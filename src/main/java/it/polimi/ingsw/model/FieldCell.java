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
        return this.occupyingWorker == null && !this.hasDome; //TODO Implementare metodo
    }

    public GameWorker getWorker() {
        return this.occupyingWorker;
    }

    public Boolean isComplete() {
        return hasDome;
    }

}
