package it.polimi.ingsw.model.exceptions;

public class AlreadyExistingNameException extends Exception{

    /**
     * thrown when the player tryes to enter with the same name as another existing player
     * @param message
     */
    public AlreadyExistingNameException(String message) {
        super(message);
    }
}
