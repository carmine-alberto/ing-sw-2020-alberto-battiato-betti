package it.polimi.ingsw.model;

import it.polimi.ingsw.actions.Action;
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

    public List<FieldCell> getAvailableCells() {
        List<FieldCell> freeCells = new ArrayList<>();
        //Controllo le celle adiacenti: se delta_height_up <= 1 AND !cell.hasDome() AND cell.isFree(), la aggiungo alla lista

        return freeCells;
    }

    public void move(Integer x, Integer y) { //TODO args non è String
        moveStrategy.run(currentGame.getCell(x, y), this);
    }

    public FieldCell getCell() {
        return this.position;
    }
}
