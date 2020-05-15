package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsCellFreePredicate implements BiPredicate<FieldCell, GameWorker> {

        @Override
        public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
            return destinationCell.isFree();
        }

        private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
            return destinationCell.getHeight() - gameWorker.getCell().getHeight();
        }

}
