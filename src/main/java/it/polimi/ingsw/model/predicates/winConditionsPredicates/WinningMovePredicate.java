package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

import static it.polimi.ingsw.utility.GameSettings.MAX_HEIGHT_WITHOUT_DOME;
import static it.polimi.ingsw.utility.GameSettings.SECOND_LEVEL;

public class WinningMovePredicate implements BiPredicate<Game, GameWorker> {


    /**
     * We check if the considered gameWorker has won, this winningMove is a move
     * from a certain level into another one.
     *
     * @param game the game we're playing on
     * @param gameWorker the gameWorker that we're checking the winCondition
     *
     * @return true if the winCondition is respected (the gameWorker has won), false otherwise
     */
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return movedFromLevel2ToLevel3(game, gameWorker);
    }

    private boolean movedFromLevel2ToLevel3(Game game, GameWorker gameWorker) {
        return gameWorker != null && gameWorker.getCell().getHeight().equals(SECOND_LEVEL)
                            &&
                game.getTurnPlayer().getPlayerState().getSelectedCell() != null
                &&
                game.getTurnPlayer().getPlayerState().getSelectedCell().getHeight().equals(MAX_HEIGHT_WITHOUT_DOME);
    }
}
