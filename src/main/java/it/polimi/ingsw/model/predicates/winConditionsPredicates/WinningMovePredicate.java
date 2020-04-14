package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class WinningMovePredicate implements BiPredicate<Game, GameWorker> {
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return movedFromLevel2ToLevel3(game, gameWorker);
    }

    private boolean movedFromLevel2ToLevel3(Game game, GameWorker gameWorker) {
        return gameWorker.getCell().getHeight() == 2
                            &&
               game.getTurnPlayer().getPlayerState().getSelectedCell().getHeight() == 3;
    }

    @Override
    public BiPredicate<Game, GameWorker> and(BiPredicate<? super Game, ? super GameWorker> other) { //TODO Implement method
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
