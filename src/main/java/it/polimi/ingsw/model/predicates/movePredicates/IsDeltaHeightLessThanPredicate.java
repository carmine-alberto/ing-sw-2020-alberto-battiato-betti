package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsDeltaHeightLessThanPredicate implements BiPredicate<FieldCell, GameWorker> {

    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
        return deltaHeight(destinationCell, gameWorker) <= 1;
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> and(BiPredicate<? super FieldCell, ? super GameWorker> other) {
        return new BiPredicate<>() {
            @Override
            public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
                return this.test(fieldCell, gameWorker) && other.test(fieldCell, gameWorker);
            }
        };
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> negate() {
        return null;
    }

    @Override
    public BiPredicate<FieldCell, GameWorker> or(BiPredicate<? super FieldCell, ? super GameWorker> other) {
        return null;
    }

}
