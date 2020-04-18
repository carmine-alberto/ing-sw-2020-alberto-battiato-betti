package it.polimi.ingsw.model.predicates.actionPredicates;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;

import java.util.function.BiPredicate;

public class ActionPredicate implements BiPredicate<ActionEnum, Player> {
    private boolean canMove;
    private boolean canBuild;
    private boolean canDisplace;

    public ActionPredicate(Boolean M, Boolean B, Boolean D){
        this.canMove = M;
        this.canBuild = B;
        this.canDisplace = D;
    }

    @Override
    public boolean test(ActionEnum actionEnum, Player player) {
        Boolean action = false;

        switch (actionEnum){
            case MOVE -> {
                action = canMove;
            }
            case BUILD -> {
                action = canBuild;
            }
            case DISPLACE -> {
                action = canDisplace;
            }
        }
        return action;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public void setCanDisplace(boolean canDisplace) {
        this.canDisplace = canDisplace;
    }

}
