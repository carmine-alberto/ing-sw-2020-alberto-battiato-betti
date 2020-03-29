package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldCell {
    Game currentGame; //TODO Set currentGame (passed to the constructor preferably)
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

    public List<FieldCell> getAdjacentCells() { //TODO Implementare metodo
        List<FieldCell> adjacentCells = new ArrayList<>();

        Integer i = Math.max(this.getPosX() - 1, 0);
        Integer k = Math.max(this.getPosY() - 1, 0);

        for (;  i <= this.getPosX() + 1 &&  i < 5 ; i++)
            for (Integer j = k; j <= this.getPosY() + 1 && j < 5; j++)
                adjacentCells.add(this.currentGame.getCell(i, j));

        adjacentCells.remove(this);

        return adjacentCells;
    }
}
