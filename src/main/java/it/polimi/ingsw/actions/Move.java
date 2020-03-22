package it.polimi.ingsw.actions;

import it.polimi.ingsw.GameWorker;
import it.polimi.ingsw.Player;

import java.util.List;

public class Move implements Action {
    Player turnPlayer;
    List<GameWorker> turnPlayerWorkers;
    @Override
    public Boolean isLegal() {
        return true; //TODO Eliminare return
    }

    @Override
    public void run(String args, GameWorker gw) {
        gw.setCell(args);
    }

}
