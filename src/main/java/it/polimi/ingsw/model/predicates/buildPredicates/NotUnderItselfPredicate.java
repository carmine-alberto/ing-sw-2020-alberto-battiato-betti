package it.polimi.ingsw.model.predicates.buildPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotUnderItselfPredicate implements BiPredicate<FieldCell, GameWorker> {

    private boolean canBuildUnderItself = false;

    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return canBuildUnderItself && fieldCell.equals(gameWorker.getCell());
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> and(BiPredicate<? super FieldCell, ? super GameWorker> other) {
        return null;
    }
}
