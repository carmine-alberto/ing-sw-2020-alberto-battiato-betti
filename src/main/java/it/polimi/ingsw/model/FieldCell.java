package it.polimi.ingsw.model;

public class FieldCell {
    GameWorker occupyingWorker;

    public boolean isFree() {
        return true; //TODO Implementare metodo
    }

    public GameWorker getWorker() {
        return this.occupyingWorker;
    }
}