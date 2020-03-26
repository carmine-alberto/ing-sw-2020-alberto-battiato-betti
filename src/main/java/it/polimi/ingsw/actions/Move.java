package it.polimi.ingsw.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class Move implements Action {
    Player turnPlayer;
    List<GameWorker> turnPlayerWorkers;
    @Override
    public Boolean isLegal() {
        return true; //TODO Eliminare return
    }

    @Override
    public void run(FieldCell cell, GameWorker gw) {
        if(isLegal()) {
            gw.setPosition(cell);
            cell.setOccupyingWorker(gw);
        }
    }

}
