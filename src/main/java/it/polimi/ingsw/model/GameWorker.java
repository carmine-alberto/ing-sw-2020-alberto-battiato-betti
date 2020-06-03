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
            this.oldMovePositions.add(cell);    //TODO empty in endphase
        }
    }

    public void move(FieldCell destination) {moveStrategy.run(destination, this);}

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
