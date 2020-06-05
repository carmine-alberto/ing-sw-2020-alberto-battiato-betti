package it.polimi.ingsw.model.predicates.constructiblePredicates;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class BlockPredicate implements BiPredicate<Player, Constructible> {
    private Integer minimumHeight;
    private static int MAX_HEIGHT = 3;

    public BlockPredicate(Integer min){
        minimumHeight = min;
    }

    /**
     * This blockPredicate checks if the constructible inserted as second paramete
     * can actually be built. If the constructible is a BLOCK the cell height has to be lowe than MAX_HEIGHT,
     * if it is a DOME it has to be at least minimumHeight (parameter set by the constructor)
     *
     * @param player the player whose worker wants to build
     * @param constructible the type of constructible that the player wants to build
     * @return true if the constructible can be built there, false otherwise.
     */
    @Override
    public boolean test(Player player, Constructible constructible) {
        Integer height = player.getPlayerState().getSelectedCell().getHeight();
        return ((constructible.equals(Constructible.BLOCK) && height < MAX_HEIGHT) || (constructible.equals(constructible.DOME) && height >= minimumHeight));
    }

}
