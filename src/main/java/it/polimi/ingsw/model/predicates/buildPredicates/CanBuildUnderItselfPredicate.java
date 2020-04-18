package it.polimi.ingsw.model.predicates.buildPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanBuildUnderItselfPredicate implements BiPredicate<FieldCell, GameWorker> {

    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !(fieldCell.getHeight() > 2) && fieldCell.equals(gameWorker.getCell());
    }

}
