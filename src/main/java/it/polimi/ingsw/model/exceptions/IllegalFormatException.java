package it.polimi.ingsw.model.exceptions;

public class IllegalFormatException extends Exception {

    /**
     * Thrown when the player sends an event containing data which can't be parsed correctly
     * @param message The detail message
     */
    public IllegalFormatException(String message) {
        super(message);
    }
}
