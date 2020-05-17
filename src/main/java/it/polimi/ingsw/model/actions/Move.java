package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class Move implements Action {
    protected Player turnPlayer;

    @Override
    public void run(FieldCell cell, GameWorker gw) {
            gw.getCell().setOccupyingWorker(null);
            gw.setPosition(cell);
    }

}
