package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class Move implements Action {
    Player turnPlayer;
    List<GameWorker> turnPlayerWorkers;

    @Override
    public void run(FieldCell cell, GameWorker gw) {
            gw.setPosition(cell);
            cell.setOccupyingWorker(gw);
    }

}
