package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableChoicesUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseActionPhase extends TurnPhase {
    List<ActionEnum> availableActions;
    Player turnPlayer;

    public ChooseActionPhase(Game currentGame) {
        super(currentGame);
        availableActions = new ArrayList(EnumSet.allOf(ActionEnum.class));
    }

    @Override
    public void stateInit() {
        nextPhase = new MovePhase(currentGame);
        turnPlayer = currentGame.getTurnPlayer();

        availableActions = availableActions
                .stream()
                .filter(action -> turnPlayer.getActionPredicate().test(action, turnPlayer))
                //.filter(actionEnum -> turnPlayer.getActionPredicate().test(turnPlayer))
                .collect(Collectors.toList());

        if (availableActions.size() > 1) {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the action to perform"));

            currentGame.notifyTurnPlayer(new AvailableChoicesUpdate(stringify(availableActions)));
        }
        else
            try {
                run(availableActions.get(0).toString());
                currentGame.endPhase();
            } catch (Exception e) {
                //Never thrown since the passed string is well-formatted
            }
    }

    @Override
    public void run(String arg) throws InvalidSelectionException {
        parseArg(arg);

        ActionEnum selectedAction = ActionEnum.valueOf(arg);

        turnPlayer.getPlayerState().setSelectedAction(selectedAction);
    }

    private void parseArg(String arg) throws InvalidSelectionException {
        if (stringify(availableActions).contains(arg))
            return;
        throw new InvalidSelectionException("The specified action is not available, try with a different one");
    }

    @Override
    public void stateEnd() {
        switch (turnPlayer.getPlayerState().getSelectedAction()) {
            case BUILD:
                nextPhase = new BuildPhase(currentGame);
                break;
            case DISPLACE: //TODO implementare lo switch del giocatore avversario
                break;
            case MOVE: break;
        }
    }


}
