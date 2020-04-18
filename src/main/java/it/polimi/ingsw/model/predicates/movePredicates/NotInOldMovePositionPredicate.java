package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotInOldMovePositionPredicate implements BiPredicate<FieldCell, GameWorker>{
    // TODO evaluate only the latest position
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !gameWorker.getOldMovePositions().contains(fieldCell);
    }
}
