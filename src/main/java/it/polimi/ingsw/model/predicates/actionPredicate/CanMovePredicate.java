package it.polimi.ingsw.model.predicates.actionPredicate;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.Predicate;

public class CanMovePredicate implements Predicate<ActionEnum> {

    @Override
    public boolean test(ActionEnum actionEnum) {
        return false;
    }

    @Override
    public Predicate<ActionEnum> and(Predicate<? super ActionEnum> other) {
        return null;
    }

    @Override
    public Predicate<ActionEnum> negate() {
        return null;
    }

    @Override
    public Predicate<ActionEnum> or(Predicate<? super ActionEnum> other) {
        return null;
    }
}


