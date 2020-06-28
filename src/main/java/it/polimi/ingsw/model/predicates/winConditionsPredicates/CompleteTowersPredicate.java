package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

import static it.polimi.ingsw.GameSettings.FIELD_SIZE;


public class CompleteTowersPredicate implements BiPredicate<Game, GameWorker> {
    private final Integer n;  // do we need getter and setter?

    public  CompleteTowersPredicate (Integer n){
        this.n = n;
    }

    /**
     * We check if the considered gameWorker has won, the completeTowersPredicate sets the player
     * as winner if the field contains at least n complete towers. The n value is set in the constructor
     *
     * @param game the game we're playing on
     * @param gameWorker the gameWorker that we're checking the winCondition
     *
     * @return true if the winCondition is respected (the gameWorker has won), false otherwise
     */
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        Integer cont = 0;

        for (Integer i = 0; i < FIELD_SIZE && cont < n; i++)
            for (Integer j = 0; j < FIELD_SIZE && cont < n; j++)
                if (game.getCell(i , j).isComplete())
                    cont++;
        return cont >= n;
    }
}
