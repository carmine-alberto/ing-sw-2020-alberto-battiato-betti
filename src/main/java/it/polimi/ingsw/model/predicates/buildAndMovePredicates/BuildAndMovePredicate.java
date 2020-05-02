package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class BuildAndMovePredicate implements BiPredicate<FieldCell, GameWorker> { // TODO remove
    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) { //test proveniente da MovePredicate
        return destinationCell.isFree() && deltaHeight(destinationCell, gameWorker) <= 1;
    }

 /*   @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) { //test proveniente da BuildPredicate
        return !fieldCell.isComplete() && !UnderItself(fieldCell, gameWorker);
    }
*/
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

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }

    private boolean UnderItself(FieldCell fieldCell, GameWorker gameWorker) {
        return fieldCell.equals(gameWorker.getCell());
    }
}
