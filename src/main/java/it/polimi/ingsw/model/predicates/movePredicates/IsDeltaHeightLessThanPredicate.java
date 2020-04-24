package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsDeltaHeightLessThanPredicate implements BiPredicate<FieldCell, GameWorker> {
    private Integer delta;

    public IsDeltaHeightLessThanPredicate(Integer delta) {
        this.delta = delta;
    }

    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
        return deltaHeight(destinationCell, gameWorker) < delta;
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }
}
