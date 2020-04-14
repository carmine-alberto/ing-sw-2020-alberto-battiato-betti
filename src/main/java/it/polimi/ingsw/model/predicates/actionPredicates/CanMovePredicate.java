package it.polimi.ingsw.model.predicates.actionPredicates;

import it.polimi.ingsw.model.Player;

import java.util.function.Predicate;

public class CanMovePredicate implements Predicate<Player> {
    private boolean CanMove = true;

    @Override
    public boolean test(Player player) {
        return false;
    }

    @Override
    public Predicate<Player> and(Predicate<? super Player> other) {
        return null;
    }

    @Override
    public Predicate<Player> or(Predicate<? super Player> other) {
        return null;
    }
}


