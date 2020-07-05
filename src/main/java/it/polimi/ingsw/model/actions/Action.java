package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

/**
 * This interface is used to implement the Strategy pattern: every action in the game is an object.
 */
public interface Action {

    /**
     * Runs a move strategy
     * @param cell The worker's destination
     * @param gw Worker which moves in the given cell
     */
    default void run(FieldCell cell, GameWorker gw) {
        return;
    };

    /**
     * Runs a build strategy
     * @param cell Where the worker builds
     * @param constructible to build
     */
    default void run(FieldCell cell, Constructible constructible) {
        return;
    };
}
