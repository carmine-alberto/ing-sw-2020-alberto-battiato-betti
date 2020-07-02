package it.polimi.ingsw.model.exceptions;

public class MaxHeightReachedException extends Exception {

    /**
     * Thrown when the player tries to build on a cell of level higher than 3
     *
     * @param message the detail message.
     */
    public MaxHeightReachedException(String message) {
        super(message);
    }
}
