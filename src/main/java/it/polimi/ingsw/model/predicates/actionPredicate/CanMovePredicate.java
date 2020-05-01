package it.polimi.ingsw.model.predicates.actionPredicate;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class CanMovePredicate implements BiPredicate<ActionEnum, Player> {

    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        if (actionEnum.equals(ActionEnum.MOVE))
            return true;
        return false;
    }
}


