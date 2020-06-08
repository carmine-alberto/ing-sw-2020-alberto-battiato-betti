package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;

public class Build implements Action {

    /**
     * runs the standard build strategy
     * @param cell where the worker builds
     * @param constructible to build
     */
    @Override
    public void run(FieldCell cell, Constructible constructible) {
        if (constructible.equals(Constructible.DOME))
            cell.placeDome();
        else
            cell.incrementHeight();

    }
}
