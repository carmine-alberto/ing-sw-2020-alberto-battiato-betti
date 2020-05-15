package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class NotOnPerimeterPredicate implements BiPredicate<FieldCell, GameWorker> { // We can use this predicate either to prevent building on perimeter or to not win moving on perimeter (e.g. Hera's opponents)
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !fieldCell.isOnPerimeter();
    }

    //TODO Create predicate to check whether a player with Hera as God Power exists (if that player loses, it will be removed from the players and that predicate should return false)
}
