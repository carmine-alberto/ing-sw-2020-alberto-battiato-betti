package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Action;
import java.util.ArrayList;
import java.util.List;

public class GameWorker {

    private Action moveStrategy;
    private Action buildStrategy;
    private FieldCell position;
    private Game currentGame;
    private Player owner;
    private List<FieldCell> oldMovePositions;
    private List<FieldCell> oldBuildPositions;

    public GameWorker(Action moveStrategy, Action buildStrategy, Game currentGame, Player owner) {
        this.moveStrategy = moveStrategy;
        this.buildStrategy = buildStrategy;
        this.currentGame = currentGame;
        this.owner = owner;
    }

    public void setPosition(FieldCell cell) {
        this.position = cell;
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
