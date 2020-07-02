package it.polimi.ingsw.model.exceptions;

public class IllegalFormatException extends Exception {

    /**
     * thrown when the player sends a non recognised message
     * @param message the detail message.
     */
    public IllegalFormatException(String message) {
        super(message);
    }
}
