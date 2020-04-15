package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseActionPhase extends TurnPhase {
    List<ActionEnum> availableActions = new ArrayList(EnumSet.allOf(ActionEnum.class));
    Player turnPlayer;

    public ChooseActionPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    public void stateInit() {
        nextPhase = new MovePhase(currentGame);
        //Notification

    }

    @Override
    public void run(String arg) {
        turnPlayer = currentGame.getTurnPlayer();

        availableActions = availableActions.stream()
                                           .filter(actionEnum -> turnPlayer.getActionPredicate().test(turnPlayer))
                                           .collect(Collectors.toList());
        // bisogna implementare anche le azioni legali (?)
        if (availableActions.size() > 1) {
            //Invio la notifica al giocatore
            //Mi metto in attesa della risposta
        }
        else
            turnPlayer.getPlayerState().setSelectedAction(ActionEnum.MOVE);

    }

    @Override
    public void stateEnd() {
        switch (turnPlayer.getPlayerState().getSelectedAction()) {
            case BUILD:
                nextPhase = new BuildPhase(currentGame);
                break;
            case DISPLACE: //TODO implementare lo switch del giocatore avversario
                break;
            case MOVE: break; // setted in stateInit()
        }
    }
}
