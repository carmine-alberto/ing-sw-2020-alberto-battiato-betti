package it.polimi.ingsw.model.predicates.constructiblePredicates;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class BlockPredicate implements BiPredicate<Player, Constructible> {
    Integer minimumHeight;

    public BlockPredicate(Integer min){
        minimumHeight = min;
    }
    @Override
    public boolean test(Player player, Constructible constructible) {
        Integer height = player.getPlayerState().getSelectedCell().getHeight();
        return ((constructible.equals(Constructible.BLOCK) && height < 3) || (constructible.equals(constructible.DOME) && height >= minimumHeight));
    }

}
