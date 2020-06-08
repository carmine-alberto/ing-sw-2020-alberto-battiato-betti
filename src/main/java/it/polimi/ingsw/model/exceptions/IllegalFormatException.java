package it.polimi.ingsw.model.exceptions;

public class IllegalFormatException extends Exception {

    /**
     * thrown when the player sends a non recognised message
     * @param message
     */
    public IllegalFormatException(String message) {
        super(message);
    }
}
