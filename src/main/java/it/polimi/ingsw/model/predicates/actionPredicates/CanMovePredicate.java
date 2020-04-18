package it.polimi.ingsw.model.predicates.actionPredicates;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CanMovePredicate implements BiPredicate<ActionEnum, Player> {

    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        if (actionEnum.equals(ActionEnum.MOVE))
            return true;
        return false;
    }
}


