package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsTurnPlayerPredicate  implements BiPredicate<Game, GameWorker> {

    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return game.getTurnPlayer().equals(gameWorker.getOwner());
    }

}
