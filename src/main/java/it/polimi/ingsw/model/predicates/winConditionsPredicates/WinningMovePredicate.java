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
        return gameWorker != null && gameWorker.getCell().getHeight() == 2
                            &&
                game.getTurnPlayer().getPlayerState().getSelectedCell() != null
                &&
               game.getTurnPlayer().getPlayerState().getSelectedCell().getHeight() == 3;
    }
}
