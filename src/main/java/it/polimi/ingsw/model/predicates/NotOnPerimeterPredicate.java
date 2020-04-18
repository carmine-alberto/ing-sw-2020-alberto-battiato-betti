package it.polimi.ingsw.model.predicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotOnPerimeterPredicate implements BiPredicate<FieldCell, GameWorker> { // We can use this predicate either to prevent building on perimeter or to not win moving on perimeter (e.g. Hera's opponents)
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !fieldCell.isOnPerimeter();
    }
}
