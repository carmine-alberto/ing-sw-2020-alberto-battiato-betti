package it.polimi.ingsw.model.exceptions;

public class MaxHeightReachedException extends Exception {

    /**
     * Thrown when the player tries to build on a cell of level higher than MAX_HEIGHT
     * @param message the detail message.
     */
    public MaxHeightReachedException(String message) {
        super(message);
    }
}
