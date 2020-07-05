package it.polimi.ingsw;

public interface Observer<T> {

    /**
     * This function updates the observer with the given message
     *
     * @param message the observer is notified with
     */
    default void update(T message) { throw new UnsupportedOperationException("Operation not available"); };

    default <V> void  update(T message, V source) { throw new UnsupportedOperationException("Operation not available");};

}
