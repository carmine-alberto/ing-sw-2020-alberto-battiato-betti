package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MovePredicate implements BiPredicate<FieldCell, GameWorker> { //TODO Remove predicate if content is kept atomized into subpredicates

    @Override
    public boolean test(FieldCell destionationCell, GameWorker gameWorker) {
       return destionationCell.isFree() && deltaHeight(destionationCell, gameWorker) <= 1;
    }

    private Integer deltaHeight(FieldCell destionationCell, GameWorker gameWorker) {
        return destionationCell.getHeight() - gameWorker.getCell().getHeight();
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
}
