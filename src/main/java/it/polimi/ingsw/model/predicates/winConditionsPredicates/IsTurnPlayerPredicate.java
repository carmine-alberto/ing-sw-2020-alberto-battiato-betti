package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class IsTurnPlayerPredicate  implements BiPredicate<Game, GameWorker> {

    /**
     * We check if the considered gameWorker has won, the isTurnPlayerPredicate sets the player
     * as winner if the gameWorker is owned by the turn player
     *
     * @param game the game we're playing on
     * @param gameWorker the gameWorker that we're checking the winCondition
     *
     * @return true if the winCondition is respected (the gameWorker has won), false otherwise
     */
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return gameWorker != null && game.getTurnPlayer().equals(gameWorker.getOwner());
    }

}
