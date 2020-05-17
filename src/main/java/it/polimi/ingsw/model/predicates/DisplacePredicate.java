package it.polimi.ingsw.model.predicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class DisplacePredicate implements BiPredicate<FieldCell , GameWorker> {
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        if (fieldCell.getWorker() == null)
            return false;

        Integer GapX = gameWorker.getCell().getPosX() - fieldCell.getPosX();
        Integer GapY = gameWorker.getCell().getPosY() - fieldCell.getPosY();

        return (gameWorker.getOwner()
                          .getCurrentGame()
                          .getCell(gameWorker.getCell().getPosX() + GapX , gameWorker.getCell().getPosY() + GapY).isFree());
    }
}
