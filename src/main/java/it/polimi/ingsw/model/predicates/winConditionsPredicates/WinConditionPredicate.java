package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class WinConditionPredicate implements BiPredicate<Game, GameWorker> {
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return false;
    }

    @Override
    public BiPredicate<Game, GameWorker> and(BiPredicate<? super Game, ? super GameWorker> other) {
        return null;
    }

    @Override
    public BiPredicate<Game, GameWorker> negate() {
        return null;
    }

    @Override
    public BiPredicate<Game, GameWorker> or(BiPredicate<? super Game, ? super GameWorker> other) {
        return null;
    }
}
