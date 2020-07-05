package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableCellsUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.predicates.DisplacePredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.ContainsOpponentWorkerPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class DisplacePhase extends TurnPhase {
    private List<FieldCell> availableCells = new ArrayList<>();

    public DisplacePhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, new ContainsOpponentWorkerPredicate().and(new DisplacePredicate()), "displacePredicate");
    }

    /**
     * Calculates the available cells and removes the turnPlayer from the game if no legal cells exist
     */
    @Override
    public void stateInit() {
        availableCells = turnPlayer
                .getPlayerState()
                .getSelectedWorker()
                .getCell()
                .getAdjacentCells()
                .stream()
                .filter(adjacentCell ->
                        phasePredicate
                                .test(adjacentCell, turnPlayer.getPlayerState().getSelectedWorker()))
                .collect(Collectors.toList());

        if (availableCells.isEmpty())
            removeTurnPlayerFromGame();
        else {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the worker to displace"));

            currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
        }
    }

    /**
     * Runs the displace action
     *
     * @param arg Selected cell coordinates
     * @throws IllegalFormatException If the string format does not fit the required one
     * @throws InvalidSelectionException If the selected cell is not among the available cells
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        FieldCell selectedCell = extractCellFromCoordinates(arg);
        if (!availableCells.contains(selectedCell))
            throw new InvalidSelectionException("The selected cell is not valid (contains no worker or the worker contained is yours)");

        displace(selectedCell);
    }


    private void displace(FieldCell selectedCell) {
        Integer gapX = turnPlayer.getPlayerState().getSelectedWorker().getCell().getPosX() - selectedCell.getPosX();
        Integer gapY = turnPlayer.getPlayerState().getSelectedWorker().getCell().getPosY() - selectedCell.getPosY();
        GameWorker opponentWorker = selectedCell.getWorker();

        FieldCell opponentWorkerFinalDestination = currentGame.getCell(turnPlayer.getPlayerState().getSelectedWorker().getCell().getPosX() + gapX, turnPlayer.getPlayerState().getSelectedWorker().getCell().getPosY() + gapY);

        opponentWorker.getCell().setOccupyingWorker(null);
        opponentWorker.setPosition(opponentWorkerFinalDestination);
    }

}

