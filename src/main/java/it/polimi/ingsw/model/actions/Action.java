package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

public interface Action {
    public void run(FieldCell cell, GameWorker gw);
    public void run(FieldCell cell, Constructible constructible);
}
