package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class ContainsOpponentWorkerPredicate implements BiPredicate<FieldCell, GameWorker> {

    /**
     * This function is used to check if a certain cell contains a worker that doesn't
     * belong to the owner of the gameWorker given as second parameter
     *
     * @param fieldCell The cell that you want to examine
     * @param gameWorker The gameWorker that is selected
     *
     * @return true if the fieldCell contains an enemy worker, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        GameWorker opponentWorker = fieldCell.getWorker();
        if (opponentWorker == null)
            return false;
        return !opponentWorker.getOwner().equals(gameWorker.getOwner());
    }
}
