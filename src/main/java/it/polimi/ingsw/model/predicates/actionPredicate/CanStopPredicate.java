package it.polimi.ingsw.model.predicates.actionPredicate;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class CanStopPredicate implements BiPredicate<ActionEnum, Player> {
    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        if (actionEnum.equals(ActionEnum.STOP))
            return true;
        return false;
    }
}
