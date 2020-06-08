package it.polimi.ingsw;

public interface Observer<T> {

    /**
     * This function updates the observer with the given message
     *
     * @param message the observer is notified with
     */
    void update(T message);

}
