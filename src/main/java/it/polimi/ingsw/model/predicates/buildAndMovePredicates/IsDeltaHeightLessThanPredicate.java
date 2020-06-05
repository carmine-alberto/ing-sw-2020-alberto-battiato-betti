package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsDeltaHeightLessThanPredicate implements BiPredicate<FieldCell, GameWorker> {
    private Integer delta;

    public IsDeltaHeightLessThanPredicate(Integer delta) {
        this.delta = delta;
    }

    /**
     * This function is used to check if the difference between two height is under the permitted value.
     * This value is used to let gameWorker go up and down from a position to another
     *
     * @param destinationCell The cell that you want to arrive to
     * @param gameWorker The selected gameWorker, from where it's starting
     *
     * @return true if the difference is under delta (set in the constructor)
     */
    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
        return (deltaHeight(destinationCell, gameWorker) < delta);// && gameWorker.getOwner().getCurrentGame().getTurnPhase().getClass();
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }
}
