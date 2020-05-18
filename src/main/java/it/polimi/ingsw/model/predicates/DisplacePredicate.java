package it.polimi.ingsw.model.predicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class DisplacePredicate implements BiPredicate<FieldCell , GameWorker> {
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        if (fieldCell.getWorker() == null)
            return false;

        Integer gapX = gameWorker.getCell().getPosX() - fieldCell.getPosX();
        Integer gapY = gameWorker.getCell().getPosY() - fieldCell.getPosY();

        try { //TODO ingconti, please love us
            return gameWorker
                    .getOwner()
                    .getCurrentGame()
                    .getCell(gameWorker.getCell().getPosX() + gapX, gameWorker.getCell().getPosY() + gapY).isFree();
        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }
    }
}