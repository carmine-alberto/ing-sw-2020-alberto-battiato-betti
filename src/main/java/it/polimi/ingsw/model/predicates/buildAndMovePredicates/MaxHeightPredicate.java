package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class MaxHeightPredicate implements BiPredicate<FieldCell , GameWorker> {
    Integer maxHeight;

    public MaxHeightPredicate(Integer n) {
        maxHeight = n;
    }

    /**
     * This function is used to check if the height of the cell is legal. The height is legal if it's
     * under maxHeight, parameter set in the constructor
     *
     * @param fieldCell The position of the cell that you want to examine
     * @param gameWorker the selected gameWorker
     *
     * @return true if the height is under maxHeight (set in the constructor)
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return fieldCell.getHeight() <= maxHeight;
    }
}
