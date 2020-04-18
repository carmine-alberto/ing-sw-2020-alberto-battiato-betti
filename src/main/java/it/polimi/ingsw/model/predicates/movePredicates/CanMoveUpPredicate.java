package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanMoveUpPredicate implements BiPredicate<FieldCell, GameWorker> {
    private boolean canMoveUp = false;
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return canMoveUp || (deltaHeight(fieldCell , gameWorker) < 1);
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }

    public void setFlag (boolean flag){ canMoveUp = flag; }

}
