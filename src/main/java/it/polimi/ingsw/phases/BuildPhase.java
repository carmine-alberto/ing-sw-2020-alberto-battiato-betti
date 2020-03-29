package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuildPhase extends TurnPhase {
    List<FieldCell> availableCells = new ArrayList<>();

    public BuildPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        this.nextPhase = new ChooseBlockPhase(currentGame);

    }

    @Override
    protected void run() {
        Player turnPlayer = currentGame.getTurnPlayer();

        availableCells = turnPlayer
                .getSelectedCell()
                .getAdjacentCells()
                .add(turnPlayer.getSelectedCell())  //Passing adjacent cells + the cell I'm on, for Zeus
                .stream()
                .filter(adjacentCell -> turnPlayer
                        .getBuildPredicate()
                        .test(adjacentCell, turnPlayer.getSelectedWorker()))
                .collect(Collectors.toList());

        if (availableCells.isEmpty()) { //TODO Currently, the player loses even if the second worker can build. Should we rollback to the ChooseWorkerPhase()?
            currentGame.removeTurnPlayer();
            setNextPhase(new ChooseWorkerPhase(currentGame));
            currentGame.setNextTurnPlayer();
        }

        //TODO Send notification to the turnPlayer
        // TODO Wait for response

        FieldCell destinationCell = turnPlayer.getSelectedCell();
        if (!availableCells.contains(destinationCell)) {
            //TODO Send notification of illegal move and let the player choose a valid cell repeating the phase - we expect this branch to be taken only by cheaters, a minority, so no Flyweight pattern is used
            setNextPhase(this);
        }
    }




    @Override
    protected void stateEnd() {

    }
}
