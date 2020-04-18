package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsDeltaHeightLessThanPredicate implements BiPredicate<FieldCell, GameWorker> {
    // volendo, si può definire un parametro Delta per permettere un "salto" più alto, per estensibilità del gioco
    @Override
    public boolean test(FieldCell destinationCell, GameWorker gameWorker) {
        return deltaHeight(destinationCell, gameWorker) <= 1;
    }

    private Integer deltaHeight(FieldCell destinationCell, GameWorker gameWorker) {
        return destinationCell.getHeight() - gameWorker.getCell().getHeight();
    }
}
