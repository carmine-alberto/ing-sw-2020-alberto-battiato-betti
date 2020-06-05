package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotOnPerimeterPredicate implements BiPredicate<FieldCell, GameWorker> { // We can use this predicate either to prevent building on perimeter or to not win moving on perimeter (e.g. Hera's opponents)

    /**
     * We check if the inserted fieldCell is not on the perimeter of the board (not depending
     * from the owner)
     *
     * @param fieldCell we're checking if it's on perimeter or not
     * @param gameWorker the gameWorker that was selected
     *
     * @return true if the considered fieldCell is not on perimeter, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !fieldCell.isOnPerimeter();
    }

    //TODO Create predicate to check whether a player with Hera as God Power exists (if that player loses, it will be removed from the players and that predicate should return false)
}
