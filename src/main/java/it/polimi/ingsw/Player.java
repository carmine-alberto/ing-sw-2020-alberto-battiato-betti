package it.polimi.ingsw;

import java.util.List;

public class Player {
    private String nickname;
    private String colour;
    private Boolean canMoveUp;
    private Boolean isWinner;
    private List<Action> availablePreMoveActions;
    private List<Action> availableMoveActions;
    private List<Action> availableBuildActions;
    private View playerView;


    private List<GameWorker> workers;


    public List<Action> getAvailableMoveActions() {
        return availableMoveActions;
    }


    public List<GameWorker> getWorkers() {
        return workers;
    }


}
