package it.polimi.ingsw;

import java.util.List;
import java.util.stream.Collectors;

public class MovePhase implements TurnState {

    @Override
    public void runPhase() {
        /*Player turnPlayer = game.getTurnPlayer();
        List<Action> legalActions = turnPlayer
                .getAvailableMoveActions().stream()
                .filter(action -> action.isLegal())
                .collect(Collectors.toList());

        for (Action a : legalActions) {
            a.run();
        }*/
    }
}
