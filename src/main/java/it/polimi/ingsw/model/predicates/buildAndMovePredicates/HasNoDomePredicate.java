package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class HasNoDomePredicate implements BiPredicate<FieldCell, GameWorker> {

    /**
     * This function is used to check if a certain cell has no dome
     *
     * @param fieldCell The cell that you want to examine
     * @param gameWorker The gameWorker that is selected
     *
     * @return true if the destinationCell has no dome, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !fieldCell.getHasDome();
    }
}
