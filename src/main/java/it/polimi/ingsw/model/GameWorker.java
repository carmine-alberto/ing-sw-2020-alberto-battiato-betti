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

    public List<FieldCell> getAdjacentCells() { //TODO Implementare metodo
        List<FieldCell> freeCells = new ArrayList<>();
        Integer i = Math.max(position.getPosX() - 1, 0);
        Integer j = Math.max(position.getPosY() - 1, 0);

        for (;  i <= position.getPosX() + 1 &&  i < 5 ; i++)
            for (; j <= position.getPosY() + 1 && j < 5; j++)
                freeCells.add(this.currentGame.getCell( i, j));

        freeCells.remove(this.position);

        return freeCells;
    }

    public void move(Integer x, Integer y) { //TODO args non è String
        moveStrategy.run(currentGame.getCell(x, y), this);
    }
    public void move(FieldCell destination) {moveStrategy.run(destination, this);} //TODO Overloading for ease of use, may be removed in the future

    public FieldCell getCell() {
        return this.position;
    }
}
