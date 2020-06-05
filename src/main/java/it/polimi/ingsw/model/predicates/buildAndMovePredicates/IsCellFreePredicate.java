package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsCellFreePredicate implements BiPredicate<FieldCell, GameWorker> {

    /**
     * This function is used to check if a certain cell is free (has no dome or worker)
     *
     * @param destinationCell The cell that you want to examine
     * @param gameWorker The gameWorker that is selected
     *
     * @return true if the destinationCell is free, false otherwise
     */
    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.isFree();
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }

}
