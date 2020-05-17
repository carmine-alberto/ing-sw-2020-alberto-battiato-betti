package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableCellsUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class MovePhase extends TurnPhase {
    private List<FieldCell> availableCells = new ArrayList<>();

   public MovePhase(Game currentGame, BiPredicate phasePredicate) {
       super(currentGame, phasePredicate, "movePredicate");
   }

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

        if (availableCells.isEmpty()) //TODO Currently, the player loses even if the second worker can move. Should we add a check and let him select the remaining one?
           removeTurnPlayerFromGame();
        else {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the destination cell"));

            currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
        }
    }

    /**
     * The adjacentCells list is passed to ease the computation (no god lets you move farther than 1 cell);
     * a more flexible solution would be passing the entire field and adding a predicate enforcing a check on the
     * distance from the current position
     * @param arg
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
