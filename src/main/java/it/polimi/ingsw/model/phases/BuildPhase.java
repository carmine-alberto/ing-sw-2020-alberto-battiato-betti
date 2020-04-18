package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableCellsUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuildPhase extends TurnPhase {
    List<FieldCell> availableCells = new ArrayList<>();
    Player turnPlayer;


    public BuildPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    public void stateInit() {
        nextPhase = new ChooseBlockPhase(currentGame);
        turnPlayer = currentGame.getTurnPlayer();

        List<FieldCell> adjacentCellsPlusSelf = turnPlayer
                .getPlayerState()
                .getSelectedCell()
                .getAdjacentCells();

        adjacentCellsPlusSelf.add(turnPlayer.getPlayerState().getSelectedCell());  //Passing adjacent cells + the cell I'm on, for Zeus

        availableCells = adjacentCellsPlusSelf
                .stream()
                .filter(adjacentCell -> turnPlayer
                        .getBuildPredicate()
                        .test(adjacentCell, turnPlayer.getPlayerState().getSelectedWorker())
                )
                .collect(Collectors.toList());

        if (availableCells.isEmpty())  //TODO Currently, the player loses even if the second worker can build. Should we rollback to the ChooseWorkerPhase()?
            removeTurnPlayerFromGame();

        currentGame.notifyTurnPlayer(new PhaseUpdate("Select the cell where you want to build"));

        currentGame.notifyTurnPlayer(new AvailableCellsUpdate(availableCells));
    }

    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        FieldCell selectedCell = extractCellFromCoordinates(arg);
        if (!availableCells.contains(selectedCell))
            throw new InvalidSelectionException("You can't build on the selected cell. Try with a different one");
        turnPlayer.getPlayerState().setSelectedCell(selectedCell);
    }




    @Override
    public void stateEnd() {

    }
}
