package it.polimi.ingsw.model.predicates.buildPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CanBuildAgainPredicate implements BiPredicate<FieldCell, GameWorker> {
    private boolean canBuildAgain = false;
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return canBuildAgain;
    }

    public void setFlag (boolean flag){ canBuildAgain = flag; }
}
