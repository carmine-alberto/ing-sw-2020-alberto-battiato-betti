package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class MaxHeightPredicate implements BiPredicate<FieldCell , GameWorker> {
    Integer maxHeight;

    public MaxHeightPredicate(Integer n) {
        maxHeight = n;
    }

    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return fieldCell.getHeight() <= maxHeight;
    }
}
