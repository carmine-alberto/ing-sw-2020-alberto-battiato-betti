package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanMoveAgainPredicate implements BiPredicate<FieldCell, GameWorker> {
    private boolean canMoveAgain = false;
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return canMoveAgain;
    }

    public void setFlag (boolean flag){ canMoveAgain = flag; }
}
