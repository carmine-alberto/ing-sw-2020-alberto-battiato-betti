package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import javafx.concurrent.Worker;

public class FieldCell {
    private GameWorker occupyingWorker;
    private Integer posX, posY, height;
    private Boolean hasDome;

    public FieldCell(Integer posX, Integer posY) {
        this.occupyingWorker = null;
        this.posX = posX;
        this.posY = posY;
        this.height = 0;
        this.hasDome = false;
    }

    public Integer getPosX(){
        return this.posX;
    }

    public Integer getPosY(){
        return this.posY;
    }

    public void setOccupyingWorker(GameWorker worker){
        this.occupyingWorker = worker;
    }

    public Integer getHeight() {
        return height;
    }

    public void incrementHeight(){
        try{
            if (height >= 3)
                throw new MaxHeightReachedException("Impossibile costruire un altro blocco. Altezza massima raggiunta.");
            height++;
        } catch (MaxHeightReachedException e) {
            //TODO Stampare il messaggio d'errore relativo
        }
    }

    public void placeDome() {
        hasDome = true;
    }

    public Boolean isOnPerimeter() {
        return posX.equals(1) || posX.equals(5) || posY.equals(1) || posY.equals(5);
    }

    public Boolean isFree() { return this.occupyingWorker == null && !this.hasDome; }

    public GameWorker getWorker() {
        return this.occupyingWorker;
    }

    public Boolean isComplete() {
        return hasDome;
    }

}
