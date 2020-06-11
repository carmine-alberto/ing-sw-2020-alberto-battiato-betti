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

public class BuildPhase extends TurnPhase {
    private List<FieldCell> availableCells = new ArrayList<>();

    public BuildPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "buildPredicate");
    }

    /**
     * Here available cells are calculated and if no cell is available the player is removed from the game
     */
    @Override
    public void stateInit() {

        List<FieldCell> adjacentCellsPlusSelf = turnPlayer
                .getPlayerState()
                .getSelectedWorker()
                .getCell()
                .getAdjacentCells();

        adjacentCellsPlusSelf.add(turnPlayer.getPlayerState().getSelectedWorker().getCell());  //Passing adjacent cells + the cell I'm on, for Zeus

        availableCells = adjacentCellsPlusSelf
                .stream()
                .filter(adjacentCell ->
                        phasePredicate
                        .test(adjacentCell, turnPlayer.getPlayerState().getSelectedWorker())
                )
                .collect(Collectors.toList());

        if (availableCells.isEmpty())
            removeTurnPlayerFromGame();
        else {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the cell where you want to build"));

            currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
        }
    }

    /**
     * Here is setted the player's selected cell
     *
     * @param arg coordinates
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection is invalid
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        FieldCell selectedCell = extractCellFromCoordinates(arg);
        if (!availableCells.contains(selectedCell))
            throw new InvalidSelectionException("You can't build on the selected cell. Try with a different one");
        turnPlayer.getPlayerState().setSelectedCell(selectedCell);
    }
}
