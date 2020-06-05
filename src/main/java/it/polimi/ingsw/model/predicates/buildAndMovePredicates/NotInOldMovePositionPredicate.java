package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotInOldMovePositionPredicate implements BiPredicate<FieldCell, GameWorker>{
    // TODO evaluate only the latest position
    /**
     * We check if the inserted fieldCell is one of the previous positions of the gameWorker
     *
     * @param fieldCell we're checking if it was the old worker position
     * @param gameWorker the gameWorker that was selected
     *
     * @return true if the considered fieldCell wasn't the last one of the considered gameWorker, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !gameWorker.getOldMovePositions().contains(fieldCell);
    }
}
