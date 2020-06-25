package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.events.BoardUpdate;
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
    private static Integer MAX_HEIGHT = 3;
    static final Integer FIELD = 5;

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

    public void setOccupyingWorker(GameWorker worker){ //TODO Are we calling this function to set the occupying worker to null when he leaves the cell?
        this.occupyingWorker = worker;
        if (worker != null && !worker.getCell().equals(this))
            worker.setPosition(this);
        currentGame.notifyObservers(new BoardUpdate(currentGame.getField()));
    }

    public Integer getHeight() {
        return height;
    }

    /**
     * This function is used to increment the fieldCell height if possible (eg. it has reached its MAX_HEIGHT) and notifies the observers
     */
    public void incrementHeight() {
        try {
            if (height >= MAX_HEIGHT)
                throw new MaxHeightReachedException("Impossibile costruire un altro blocco. Altezza massima raggiunta.");
            height++;
            currentGame.notifyObservers(new BoardUpdate(currentGame.getField()));
        } catch (MaxHeightReachedException e) {
            //TODO Stampare il messaggio d'errore relativo
        }
    }

    /**
     * This function places a dome in the selected fieldCell and notifies the observers.
     */
    public void placeDome() {
        hasDome = true;
        currentGame.notifyObservers(new BoardUpdate(currentGame.getField()));
    }

    /**
     * This function return true if the fieldCell is on the perimeter of the board
     *
     * @return true if it's on perimeter
     */
    public Boolean isOnPerimeter() {
        return posX.equals(0) || posX.equals(4) || posY.equals(0) || posY.equals(4);
    }

    /**
     * A cell is considered to be free if has no worker on it and is has no dome
     *
     * @return true if the cell is free, false otherwise
     */
    public Boolean isFree() { return this.occupyingWorker == null && !this.hasDome; } // una cella è libera se non c'è ne cupola ne worker

    /**
     * A cell is complete if it has reached its maximum height and it has a dome
     *
     * @return tre if the cell is complete, false otherwise
     */
    public Boolean isComplete() {
        return this.hasDome && this.getHeight() == MAX_HEIGHT;
    }

    public Boolean getHasDome() {
        return this.hasDome;
    }

    public GameWorker getWorker() {
        return this.occupyingWorker;
    }

    /**
     * The adjacentCells are the cells surrounding the selected one.
     *
     * @return the list of the cell's adjacent cells
     */
    public List<FieldCell> getAdjacentCells() {
        List<FieldCell> adjacentCells = new ArrayList<>();

        Integer i = Math.max(this.getPosX() - 1, 0);
        Integer k = Math.max(this.getPosY() - 1, 0);

        for (;  i <= this.getPosX() + 1 &&  i < FIELD ; i++)
            for (Integer j = k; j <= this.getPosY() + 1 && j < FIELD; j++)
                adjacentCells.add(this.currentGame.getCell(i, j));

        adjacentCells.remove(this);

        return adjacentCells;
    }

    //Todo: javadoc on this one ?
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
