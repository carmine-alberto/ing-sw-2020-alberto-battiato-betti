package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

public interface Action {
    default void run(FieldCell cell, GameWorker gw) {
        return;
    };    //TODO Should throw an Exception
    default void run(FieldCell cell, Constructible constructible) {
        return;
    };
}
