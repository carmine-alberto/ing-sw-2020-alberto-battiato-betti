package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    protected final List<Observer<T>> observers = new ArrayList<>();

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

    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

    protected void notify(Observer<T> specificObserver, T message){
        synchronized (observers) {
            specificObserver.update(message);
        }
    }

}
