package it.polimi.ingsw.model.exceptions;

public class MaxHeightReachedException extends Exception {

    /**
     * thrown when the player tryes to build on a cell of level higher than 3
     * @param message
     */
    public MaxHeightReachedException(String message) {
        super(message);
    }
}
