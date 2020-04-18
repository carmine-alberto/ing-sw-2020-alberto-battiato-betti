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
        return false; // la soluzione più efficiente sarebbe creare una variabile in game che tiene conto del numero di torri complete, l'alternativa è chiamare una funzione che scorra le celle per calcolare il numero ma sembra più onerosa
    }
}
