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
    public void run(FieldCell cell, GameWorker gw) {
        FieldCell currentWorkerPosition = gw.getCell();
        GameWorker opponentWorker;

        //TODO aggiungere alle legalità il controllo che il worker sia avversario

        if (!cell.isFree()) { //fase di swap (due worker nella stessa cella)
            opponentWorker = cell.getWorker();
            opponentWorker.setPosition(currentWorkerPosition);
        }

        super.run(cell, gw);

    }
}
