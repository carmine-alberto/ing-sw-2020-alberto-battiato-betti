package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class ContainsOpponentWorkerPredicate implements BiPredicate<FieldCell, GameWorker> {
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        GameWorker opponentWorker = fieldCell.getWorker();
        if (opponentWorker == null)
            return false;
        return !opponentWorker.getOwner().equals(gameWorker.getOwner());
    }
}
