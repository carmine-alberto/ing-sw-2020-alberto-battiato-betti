package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.Move;

public class MoveAndShiftBack extends Move implements Action {
    @Override
    public void run(FieldCell cell, GameWorker gw){

        if (!cell.isFree()) {
            Integer gapX = cell.getPosX() - gw.getCell().getPosX();
            Integer gapY = cell.getPosY() - gw.getCell().getPosY();
            GameWorker opponentWorker = cell.getWorker();
            FieldCell destination = opponentWorker.getOwner().getCurrentGame().getCell(opponentWorker.getCell().getPosX() + gapX, opponentWorker.getCell().getPosY() + gapY);
            super.run(destination, opponentWorker);
        }
        super.run(cell, gw);
    }
}
