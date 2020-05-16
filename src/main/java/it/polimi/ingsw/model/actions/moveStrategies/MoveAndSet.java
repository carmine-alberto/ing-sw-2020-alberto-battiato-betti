package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.Move;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class MoveAndSet extends Move implements Action {
    private BiPredicate<FieldCell, GameWorker> predicate;
    private List<Player> opponents;
    HashMap<Player, BiPredicate<FieldCell, GameWorker>> oldPredicates;
    //private boolean application;

    public MoveAndSet(BiPredicate<FieldCell, GameWorker> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void run(FieldCell cell, GameWorker gw) {

        if (cell.getHeight() - gw.getCell().getHeight() < 1)   // if gw doesn't move up...
            gw.getOwner().getSelectedGod().setOuterPredicate("movePredicate", null);
        else
            gw.getOwner().getSelectedGod().setOuterPredicate("movePredicate", predicate); //todo ci sono dei casi in cui sovrascrive (gestire and e or)

        super.run(cell, gw);
    }

    private void setPredicates(GameWorker gw) {   //this is working only for opponent workers at the moment

        if (!opponents.isEmpty())
            setup(gw);

        oldPredicates.forEach((player, predicate) -> player.getSelectedGod().setMovePredicates(this.predicate.and(predicate)));
    }

    private void restorePredicates() {
            oldPredicates.forEach((player, predicate) -> player.getSelectedGod().setMovePredicates(predicate));
    }

    private void setup (GameWorker gw){
        opponents = gw
                .getOwner()
                .getCurrentGame()
                .getPlayers()
                .stream()
                .filter(player -> !player.equals(gw.getOwner()))
                .collect(Collectors.toList());

        opponents.forEach(player -> oldPredicates.put(player, player.getSelectedGod().getMovePredicates()));
    }

}

