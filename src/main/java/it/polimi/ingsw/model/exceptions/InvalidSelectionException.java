package it.polimi.ingsw.model.exceptions;

public class InvalidSelectionException extends Exception {

    /**
     * thrown when the player tryes to do an invalid selection
     * @param message
     */
    public InvalidSelectionException(String message) {
        super(message);
    }

}
