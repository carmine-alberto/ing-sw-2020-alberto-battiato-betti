package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanMoveBackPredicate implements BiPredicate<FieldCell, GameWorker> {

    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        if (fieldCell.isOnPerimeter() && !gameWorker.getCell().isOnPerimeter())
                return false;

        Integer gapX = fieldCell.getPosX() - gameWorker.getCell().getPosX();
        Integer gapY = fieldCell.getPosY() - gameWorker.getCell().getPosY();

        try { //TODO ingconti, please love us
            return gameWorker
                    .getOwner()
                    .getCurrentGame()
                    .getCell(fieldCell.getPosX() + gapX, fieldCell.getPosY() + gapY).isFree();
        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }
    }
}
