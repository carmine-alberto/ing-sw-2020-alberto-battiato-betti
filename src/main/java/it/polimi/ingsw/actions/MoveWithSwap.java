package it.polimi.ingsw.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

public class MoveWithSwap extends Move implements Action {
    @Override
    public Boolean isLegal() {
        return true;
    }

    @Override
    public void run(String args, GameWorker gw) {
        FieldCell currentWorkerPosition = gw.getCell();
        FieldCell destinationCell = Game.getCell(args);
        GameWorker opponentWorker;

        if (!destinationCell.isFree()) {
            opponentWorker = destinationCell.getWorker();
            opponentWorker.setCell(new String("CurrentWorkerPosition")); //TODO passare FieldCell
        }

        super.run(args, gw);

    }
}
