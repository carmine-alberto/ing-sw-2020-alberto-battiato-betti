package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Action;
import java.util.ArrayList;
import java.util.List;

public class GameWorker {

    Action moveStrategy;
    FieldCell position;
    Game currentGame;

    public GameWorker(Action moveStrategy, Game currentGame) {
        this.moveStrategy = moveStrategy;
        this.currentGame = currentGame;
    }

    public void setPosition(FieldCell cell) {
        this.position = cell;
    }


    public void move(Integer x, Integer y) { //TODO args non è String
        moveStrategy.run(currentGame.getCell(x, y), this);
    }
    public void move(FieldCell destination) {moveStrategy.run(destination, this);} //TODO Overloading for ease of use, may be removed in the future

    public FieldCell getCell() {
        return this.position;
    }
}
