package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldCell {
    Game currentGame;
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
    };

    public void incrementHeight() throws MaxHeightReachedException {
        if (height < 3)
            height++;
        else
            throw new MaxHeightReachedException();
    };

    public void placeDome() {
        hasDome = true;
    };

    public Boolean isOnPerimeter() {
        return posX.equals(1) || posX.equals(5) || posY.equals(1) || posY.equals(5);
    };

    public Boolean isFree() {
        return this.occupyingWorker == null && !this.hasDome;
    }

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

    public List<FieldCell> getAdjacentCells(FieldCell targetCell) { //TODO Implementare metodo
        List<FieldCell> freeCells = new ArrayList<>();
        Integer i = Math.max(targetCell.getPosX() - 1, 0);
        Integer j = Math.max(targetCell.getPosY() - 1, 0);

        for (;  i <= targetCell.getPosX() + 1 &&  i < 5 ; i++)
            for (; j <= targetCell.getPosY() + 1 && j < 5; j++)
                freeCells.add(currentGame.getCell( i, j));

        freeCells.remove(targetCell);

        return freeCells;
    }
}
