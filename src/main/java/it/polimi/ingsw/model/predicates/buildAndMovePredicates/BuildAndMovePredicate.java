package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class BuildAndMovePredicate implements BiPredicate<FieldCell, GameWorker> { // TODO remove
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !fieldCell.isComplete() && !UnderItself(fieldCell, gameWorker);
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> and(BiPredicate<? super FieldCell, ? super GameWorker> other) {
        return null;
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> negate() {
        return null;
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> or(BiPredicate<? super FieldCell, ? super GameWorker> other) {
        return null;
    }

    private boolean UnderItself(FieldCell fieldCell, GameWorker gameWorker) {
        return fieldCell.equals(gameWorker.getCell());
    }

}
