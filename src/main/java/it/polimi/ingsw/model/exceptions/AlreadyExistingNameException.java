package it.polimi.ingsw.model.exceptions;

public class AlreadyExistingNameException extends Exception{

    /**
     * Thrown when the player tries to log in with the same name as a different existing player
     * @param message The detail message
     */
    public AlreadyExistingNameException(String message) {
        super(message);
    }
}
