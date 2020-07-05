package it.polimi.ingsw.model.exceptions;

public class InvalidSelectionException extends Exception {

    /**
     * Thrown when the player sends an event containing data which can be parsed but is not accepted by the current gameState
     * @param message the detail message.
     */
    public InvalidSelectionException(String message) {
        super(message);
    }

}
