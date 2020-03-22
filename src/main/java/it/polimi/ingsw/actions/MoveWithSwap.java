package it.polimi.ingsw.actions;

import it.polimi.ingsw.FieldCell;
import it.polimi.ingsw.Game;
import it.polimi.ingsw.GameWorker;

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
