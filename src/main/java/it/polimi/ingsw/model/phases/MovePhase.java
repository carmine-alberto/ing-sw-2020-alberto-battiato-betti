package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableCellsUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class MovePhase extends TurnPhase {
    private List<FieldCell> availableCells = new ArrayList<>();

   public MovePhase(Game currentGame, BiPredicate phasePredicate) {
       super(currentGame, phasePredicate, "movePredicate");
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
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the destination cell"));

            currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
        }
    }

    /**
     * Sets the player's selected cell
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
            throw new InvalidSelectionException("The selected cell cannot be reached. Try with a different one");
        turnPlayer.getPlayerState().setSelectedCell(selectedCell);

        checkWinConditions(); //Current worker position is the starting position

        turnPlayer.getPlayerState().getSelectedWorker().move(selectedCell);
    }

}
