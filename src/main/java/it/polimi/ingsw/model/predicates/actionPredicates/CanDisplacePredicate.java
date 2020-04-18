package it.polimi.ingsw.model.predicates.actionPredicates;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class CanDisplacePredicate implements BiPredicate<ActionEnum, Player> {

    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        if (actionEnum.equals(ActionEnum.DISPLACE))
            return true;
        return false;
    }
}
