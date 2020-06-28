package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class HasMovedDownPredicate implements BiPredicate<Game, GameWorker> {
    private final Integer minimumDescent; // do we need getter and setter?

    public HasMovedDownPredicate(Integer n){
        this.minimumDescent = n;
    }

    /**
     * We check if the considered gameWorker has won, the hasMovedDownPredicate sets the player
     * as winner if the worker has moved down by at least n levels (the number n is set in the constructor)
     *
     * @param game the game we're playing on
     * @param gameWorker the gameWorker that we're checking the winCondition
     *
     * @return true if the winCondition is respected (the gameWorker has won), false otherwise
     */
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return gameWorker != null && game.getTurnPlayer().getPlayerState().getSelectedCell() != null && (gameWorker.getCell().getHeight() >= game.getTurnPlayer().getPlayerState().getSelectedCell().getHeight() + minimumDescent);
    }

}
