package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

public class Move implements Action {
    protected Player turnPlayer;    //TODO remove

    /**
     * runs the standard move strategy
     * @param cell where the worker moves
     * @param gw worker which moves in the given cell
     */
    @Override
    public void run(FieldCell cell, GameWorker gw) {
            gw.getCell().setOccupyingWorker(null);
            gw.setPosition(cell);
    }

}
