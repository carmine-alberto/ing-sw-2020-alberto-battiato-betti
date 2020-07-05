package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.Move;

import java.util.function.BiPredicate;

public class MoveAndSet extends Move implements Action {
    private BiPredicate<FieldCell, GameWorker> predicate;
 
    public MoveAndSet(BiPredicate<FieldCell, GameWorker> predicate) {
        this.predicate = predicate;
    }

    /**
     * This strategy sets an "outerPredicate" to the current TurnPlayer.
     * @param cell where the worker moves
     * @param gw which moves
     */
    @Override
    public void run(FieldCell cell, GameWorker gw) {

        if (cell.getHeight() - gw.getCell().getHeight() < 1)   // if gw doesn't move up...
            gw.getOwner().getSelectedGod().setOuterPredicate("movePredicate", null);
        else
            gw.getOwner().getSelectedGod().setOuterPredicate("movePredicate", predicate);

        super.run(cell, gw);
    }
}

