package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseActionPhase extends TurnPhase {
    List<ActionEnum> availableActions = new ArrayList<ActionEnum>(EnumSet.allOf(ActionEnum.class));
    Player turnPlayer;

    public ChooseActionPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        nextPhase = new MovePhase(currentGame);
        //Notification

    }

    @Override
    protected void run() {
        turnPlayer = currentGame.getTurnPlayer();

        availableActions = availableActions.stream()
                                           .filter(actionEnum -> turnPlayer.getActionPredicate().test(turnPlayer))
                                           .collect(Collectors.toList());

        if (availableActions.size() > 1) {
            //Invio la notifica al giocatore
            //Mi metto in attesa della risposta
        }
        else
            turnPlayer.setSelectedAction(ActionEnum.MOVE);

    }

    @Override
    protected void stateEnd() {
        switch (turnPlayer.getSelectedAction()) {
            case BUILD:
                nextPhase = new BuildPhase(currentGame);
                break;
            case DISPLACE: //TODO implementare lo switch del giocatore avversario
                break;
            case MOVE: break; // settato in stateinit()
        }
    }
}
