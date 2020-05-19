package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;

import java.util.function.BiPredicate;

public class HasMovedDownPredicate implements BiPredicate<Game, GameWorker> {
    private Integer minimumDescent; // do we need getter and setter?

    public HasMovedDownPredicate(Integer n){
        this.minimumDescent = n;
    }
    @Override
    public boolean test(Game game, GameWorker gameWorker) {
        return gameWorker != null && game.getTurnPlayer().getPlayerState().getSelectedCell() != null && (gameWorker.getCell().getHeight() >= game.getTurnPlayer().getPlayerState().getSelectedCell().getHeight() + minimumDescent);
    }

}
