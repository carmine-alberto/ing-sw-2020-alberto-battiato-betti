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

    public GameWorker() {};

    public GameWorker(Action moveStrategy, Action buildStrategy, Game currentGame, Player owner) {
        this.moveStrategy = moveStrategy;
        this.buildStrategy = buildStrategy;
        this.currentGame = currentGame;
        this.owner = owner;
    }

    public void setPosition(FieldCell cell) {
        this.position = cell;
        if (!this.equals(cell.getWorker())) {
            cell.setOccupyingWorker(this);
            this.oldMovePositions.add(cell);    //TODO empty in endphase
        }
    }


    public void move(Integer x, Integer y) { //TODO args non è String
        moveStrategy.run(currentGame.getCell(x, y), this);
    }
    public void move(FieldCell destination) {moveStrategy.run(destination, this);} //TODO Overloading for ease of use, may be removed in the future

    public void build(FieldCell destination, Constructible constructible) {
        buildStrategy.run(destination, constructible);
    }

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
