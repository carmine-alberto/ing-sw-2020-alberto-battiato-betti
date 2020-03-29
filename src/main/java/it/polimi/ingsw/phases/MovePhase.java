package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.animation.KeyFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovePhase extends TurnPhase {
    List<FieldCell> availableCells = new ArrayList<>();

    public MovePhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        nextPhase = new BuildPhase(currentGame);

    }

    /**
     * The adjacentCells list is passed to ease the computation (no god lets you move farther than 1 cell);
     * a more flexible solution would be passing the entire field and adding a predicate enforcing a check on the
     * distance from the current position
     */
    @Override
    protected void run() {
        Player turnPlayer = currentGame.getTurnPlayer();

        availableCells = turnPlayer
                .getSelectedWorker()
                .getCell()
                .getAdjacentCells()
                .stream()
                .filter(adjacentCell -> turnPlayer
                        .getMovePredicate()
                        .test(adjacentCell, turnPlayer.getSelectedWorker()))
                .collect(Collectors.toList());

        if (availableCells.isEmpty()) { //TODO Currently, the player loses even if the second worker can move. Should we add a check and let him select the remaining one?
            currentGame.removeTurnPlayer();
            setNextPhase(new ChooseWorkerPhase(currentGame));
            currentGame.setNextTurnPlayer();
        }

        //TODO Send notification to turnPlayer containing availableCells
        //TODO Wait for response

        FieldCell destinationCell = turnPlayer.getSelectedCell();
        if (availableCells.contains(destinationCell))
            turnPlayer.getSelectedWorker().move(destinationCell);
        else {
            //TODO Send notification of illegal move and let the player choose a valid cell repeating the phase - we expect this branch to be taken only by cheaters, a minority, so no Flyweight pattern is used
            setNextPhase(this);
        }



    }

    @Override
    protected void stateEnd() {
        //Se ho un predicato che agisce a fine turno -> eseguo una determinata azione

    }
}
