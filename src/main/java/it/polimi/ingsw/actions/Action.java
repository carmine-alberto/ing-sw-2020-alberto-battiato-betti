package it.polimi.ingsw.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

public interface Action {
    public Boolean isLegal();
    public void run(FieldCell cell, GameWorker gw);
}
