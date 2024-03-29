package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.Move;

public class MoveAndSwap extends Move implements Action {

    /**
     * This strategy makes the given worker swap position with the given cell's occupying worker
     * @param cell Where the worker moves
     * @param gw The worker that moves to the given cell
     */
    @Override
    public void run(FieldCell cell, GameWorker gw) {

        if (cell.isFree())
            super.run(cell, gw);
        else {
            GameWorker opponentWorker = cell.getWorker();
            FieldCell oldPos = gw.getCell();
            cell.setOccupyingWorker(null);
            oldPos.setOccupyingWorker(null);
            gw.setPosition(cell);
            opponentWorker.setPosition(oldPos);
        }
    }
}
