package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotInOldBuildPositionPredicate implements BiPredicate<FieldCell, GameWorker> {
    /**
     * We check if the inserted fieldCell is one of the previous positions where the gameWorker built
     *
     * @param fieldCell we're checking if it was the old worker's build position
     * @param gameWorker the gameWorker that was selected
     *
     * @return true if the considered fieldCell wasn't the last build position of the considered gameWorker, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !gameWorker.getOldBuildPositions().contains(fieldCell);
    }
}
