package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanMoveBackPredicate implements BiPredicate<FieldCell, GameWorker> {

    /**
     * This function is used to check if a given worker can make another one
     * move back from its position (fieldCell) into another cell in line with the one
     * where the gameWorker is occupying
     *
     * @param fieldCell the cell examined (that should contain another worker)
     * @param gameWorker the gameWorker that should move back the other worker
     *
     * @return true if the other worker can move back, false otherwise
     */
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
