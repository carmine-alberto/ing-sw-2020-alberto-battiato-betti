package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

public interface Action {

    /**
     * runs a move strategy
     * @param cell where the worker moves
     * @param gw worker which moves in the given cell
     */
    default void run(FieldCell cell, GameWorker gw) {
        return;
    };    //TODO Should throw an Exception

    /**
     * runs a build strategy
     * @param cell where the worker builds
     * @param constructible to build
     */
    default void run(FieldCell cell, Constructible constructible) {
        return;
    };
}
