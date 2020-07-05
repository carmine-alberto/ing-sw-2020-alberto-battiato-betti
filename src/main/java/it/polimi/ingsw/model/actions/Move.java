package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

/**
 * This class offers basic move behaviour and is used to implement a pseudo-Decorator pattern:
 * its subclasses call the parent's run() method and add action-specific behaviour afterwards
 */
public class Move implements Action {
    protected Player turnPlayer;    //TODO remove

    @Override
    public void run(FieldCell cell, GameWorker gw) {
            gw.getCell().setOccupyingWorker(null);
            gw.setPosition(cell);
    }

}
