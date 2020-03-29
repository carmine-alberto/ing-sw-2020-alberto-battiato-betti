package it.polimi.ingsw.model.predicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsDeltaHeightLessThanPredicate implements BiPredicate<FieldCell, GameWorker> {

    @Override
    public boolean test(FieldCell destionationCell, GameWorker gameWorker) {
        return deltaHeight(destionationCell, gameWorker) <= 1;
    }

    private Integer deltaHeight(FieldCell destionationCell, GameWorker gameWorker) {
        return destionationCell.getHeight() - gameWorker.getCell().getHeight();
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
