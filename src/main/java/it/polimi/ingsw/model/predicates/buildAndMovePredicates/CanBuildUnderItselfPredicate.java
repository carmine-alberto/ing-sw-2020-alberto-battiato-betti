package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

import static it.polimi.ingsw.GameSettings.Y_STARTING_POSITION;

public class CanBuildUnderItselfPredicate implements BiPredicate<FieldCell, GameWorker> {

    /**
     * This function is used to check if a certain gameWorker can build a BLOCK under itself
     *
     * @param fieldCell The considered cell
     * @param gameWorker The gameWorker that is now building
     *
     * @return true if the worker can build, false otherwise
     */
    @Override
    public boolean test(FieldCell fieldCell, GameWorker gameWorker) {
        return !(fieldCell.getHeight() > Y_STARTING_POSITION) && fieldCell.equals(gameWorker.getCell());
    }

}
