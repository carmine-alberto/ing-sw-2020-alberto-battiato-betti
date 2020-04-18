package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanMoveBackPredicate implements BiPredicate<FieldCell, GameWorker> {

    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        if (fieldCell.isOnPerimeter())
                return false;

        Integer gapX = fieldCell.getPosX() - gameWorker.getCell().getPosX();
        Integer gapY = fieldCell.getPosY() - gameWorker.getCell().getPosY();

        return gameWorker.getOwner().getCurrentGame().getCell(fieldCell.getPosX() + gapX, fieldCell.getPosY() + gapY).isFree();
    }
}
