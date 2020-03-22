package it.polimi.ingsw.actions;

import it.polimi.ingsw.GameWorker;

public interface Action {
    public Boolean isLegal();
    public void run(String args, GameWorker gw);
}
