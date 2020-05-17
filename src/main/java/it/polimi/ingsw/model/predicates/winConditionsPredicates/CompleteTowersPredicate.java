package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class CompleteTowersPredicate implements BiPredicate<Game, GameWorker> {
    private Integer n;  // do we need getter and setter?
    public static Integer FIELD_SIZE = 5;

    public  CompleteTowersPredicate (Integer n){
        this.n = n;
    }
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
