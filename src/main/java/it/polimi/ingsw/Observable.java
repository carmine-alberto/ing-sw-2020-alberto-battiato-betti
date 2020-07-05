package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    protected final List<Observer<T>> observers = new ArrayList<>();

    /**
     * This functions adds an'observers to the observers list
     *
     * @param observer that you want to add to the list
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }


    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * This function gives all the observers the inserted message
     *
     * @param message You want the observer to be notified with
     */
    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

    protected <V> void notify(T message, V source) {
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message, source);
            }
        }

    }

    /**
     * This function gives a specific observer the inserted message
     *
     * @param specificObserver the observer you want to be notified
     * @param message the message you want to notify
     */
    protected void notify(Observer<T> specificObserver, T message){
        synchronized (observers) {
            specificObserver.update(message);
        }
    }

}
