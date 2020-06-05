package it.polimi.ingsw.model.predicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class DisplacePredicate implements BiPredicate<FieldCell , GameWorker> {

    /**
     * We check if the displace can be done in the considered fieldCell form the considered gameWorker
     *
     * @param fieldCell the fieldCell on which we're considering if the displace is possible
     * @param gameWorker the gameWorker that has to displace
     * @return
     */
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
