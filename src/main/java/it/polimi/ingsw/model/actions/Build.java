package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;

/**
 * This class offers basic build behaviour and is used to implement a pseudo-Decorator pattern:
 * its subclasses call the parent's run() method and add action-specific behaviour afterwards
 */
public class Build implements Action {

    @Override
    public void run(FieldCell cell, Constructible constructible) {
        if (constructible.equals(Constructible.DOME))
            cell.placeDome();
        else
            cell.incrementHeight();
    }
}
