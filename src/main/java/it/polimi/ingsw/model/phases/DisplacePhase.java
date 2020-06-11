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
        super(currentGame, new ContainsOpponentWorkerPredicate().and(new DisplacePredicate()), "displacePredicate"); //TODO We are hardcoding the predicate here - a better solution would be getting the predicate from the godPowers file, but it's pointless.
    }

    /**
     * Here available cells are calculated and if no cell is available the player is removed from the game
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

        if (availableCells.isEmpty())  //TODO Is this solution accepted? Testing the available actions is feasible but requires visiting deeper graph nodes - if our solution works, I ain't doing it
            removeTurnPlayerFromGame();
        else {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the worker to displace"));

            currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
        }
    }

    /**
     * Here the displace action is run
     *
     * @param arg coordinates
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection of the action is invalid
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        FieldCell selectedCell = extractCellFromCoordinates(arg);
        if (!availableCells.contains(selectedCell))
            throw new InvalidSelectionException("The selected cell is not valid (contains no worker or the worker contained is yours)");

        displace(selectedCell); //TODO Hardcoded as well, read comment above
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

