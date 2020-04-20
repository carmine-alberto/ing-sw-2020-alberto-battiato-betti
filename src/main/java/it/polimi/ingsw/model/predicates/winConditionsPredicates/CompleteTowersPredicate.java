package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CompleteTowersPredicate implements BiPredicate<Game, GameWorker> {
    private Integer n;  // do we need getter and setter?

    public  CompleteTowersPredicate (Integer n){
        this.n = n;
    }
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        Integer cont = 0;

        for (Integer i = 0; i < 5; i++)
            for (Integer j = 0; i < 5 && cont < n; i++)
                if (game.getCell(i , j).isComplete())
                    cont++;

        return (cont >= n);
    }
}
