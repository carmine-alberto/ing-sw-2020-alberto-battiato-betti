package it.polimi.ingsw.model.predicates.actionPredicate;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class CanDisplacePredicate implements BiPredicate<ActionEnum, Player> {

    /**
     * This function is used to check if the player can choose to displace
     *
     * @param actionEnum The possibility given to the player
     * @param player The player that is playing
     *
     * @return true if the player can chose to displace, false otherwise
     */
    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        if (actionEnum.equals(ActionEnum.DISPLACE))
            return true;
        return false;
    }
}
