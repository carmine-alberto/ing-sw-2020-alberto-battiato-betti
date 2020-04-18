package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

public class Build implements Action {
    @Override
    public void run(FieldCell cell, GameWorker gw) {return;}

    @Override
    public void run(FieldCell cell, Constructible constructible) {
        if (constructible.equals(Constructible.DOME))
            cell.placeDome();
        else
            cell.incrementHeight();

    }
}
