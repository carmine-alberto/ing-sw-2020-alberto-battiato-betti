package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameWorker implements Serializable  {

    private transient Action moveStrategy;
    private transient Action buildStrategy;
    private FieldCell position;
    private transient Game currentGame;
    private Player owner;
    private transient List<FieldCell> oldMovePositions = new ArrayList<>();
    private transient List<FieldCell> oldBuildPositions= new ArrayList<>();

    public GameWorker(Game currentGame, Player owner) {
        this.currentGame = currentGame;
        this.owner = owner;
        this.moveStrategy = owner.getSelectedGod().getMoveStrategy();
        this.buildStrategy = owner.getSelectedGod().getBuildStrategy();
    }

    public void setPosition(FieldCell cell) {
        this.position = cell;
        if (!this.equals(cell.getWorker())) {
            cell.setOccupyingWorker(this);
            this.oldMovePositions.add(cell);
        }
    }

    /**
     * Moves the gameWorker to the FieldCell passed as argument
     * @param destination A FieldCell that will be set as the one the gameWorker dwells
     */
    public void move(FieldCell destination) {moveStrategy.run(destination, this);}

    /**
     * This function is used to get the selected gameWorker to build in the destination passed as first parameter
     * @param destination The fieldCell you want to build on
     * @param constructible The constructible type you want to build
     */
    public void build(FieldCell destination, Constructible constructible) {
        buildStrategy.run(destination, constructible);
    }

    /**
     * This function is used to get the position of the worker
     * @return The fieldCell the worker is occupying
     */
    public FieldCell getCell() {
        return this.position;
    }

    public List<FieldCell> getOldMovePositions() {
        return oldMovePositions;
    }

    public List<FieldCell> getOldBuildPositions() {
        return oldBuildPositions;
    }

    public Player getOwner() {
        return owner;
    }


}
