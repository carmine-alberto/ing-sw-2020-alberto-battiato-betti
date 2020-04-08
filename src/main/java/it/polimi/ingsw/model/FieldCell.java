package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import javafx.concurrent.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldCell implements Serializable {
    private transient Game currentGame;
    private GameWorker occupyingWorker;
    private Integer posX, posY, height;
    private Boolean hasDome;

    public FieldCell(Game currentGame, Integer posX, Integer posY) {
        this.occupyingWorker = null;
        this.posX = posX;
        this.posY = posY;
        this.height = 0;
        this.hasDome = false;
        this.currentGame = currentGame;
    }

    public Integer getPosX(){
        return this.posX;
    }

    public Integer getPosY(){
        return this.posY;
    }

    public void setOccupyingWorker(GameWorker worker){
        this.occupyingWorker = worker;
        if (!worker.getCell().equals(this))
            worker.setPosition(this);
        currentGame.notifyObservers();
    }


    public Integer getHeight() {
        return height;
    }

    public void incrementHeight() {
        try {
            if (height >= 3)
                throw new MaxHeightReachedException("Impossibile costruire un altro blocco. Altezza massima raggiunta.");
            height++;
            currentGame.notifyObservers();
        } catch (MaxHeightReachedException e) {
            //TODO Stampare il messaggio d'errore relativo
        }
    }


    public void placeDome() {
        hasDome = true;
        currentGame.notifyObservers();
    }


    public Boolean isOnPerimeter() {
        return posX.equals(0) || posX.equals(4) || posY.equals(0) || posY.equals(4);
    }

    public Boolean isFree() { return this.occupyingWorker == null && !this.hasDome; }

    public Boolean isComplete() {
        return this.hasDome && this.getHeight()==3;
    }

    public Boolean getHasDome() {
        return this.hasDome;
    }


    public GameWorker getWorker() {
        return this.occupyingWorker;
    }

    public List<FieldCell> getAdjacentCells() {
        List<FieldCell> adjacentCells = new ArrayList<>();

        Integer i = Math.max(this.getPosX() - 1, 0);
        Integer k = Math.max(this.getPosY() - 1, 0);

        for (;  i <= this.getPosX() + 1 &&  i < 5 ; i++)
            for (Integer j = k; j <= this.getPosY() + 1 && j < 5; j++)
                adjacentCells.add(this.currentGame.getCell(i, j));

        adjacentCells.remove(this);

        return adjacentCells;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FieldCell fieldCell = (FieldCell) o;
        return posX.equals(fieldCell.posX) &&
                posY.equals(fieldCell.posY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }


}
