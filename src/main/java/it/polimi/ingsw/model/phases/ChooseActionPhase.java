package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

public class ChooseActionPhase extends TurnPhase {
    List<String> availableActions = new ArrayList<>();

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
        //Scorro i predicati per vedere quali azioni sono disponibili (Caronte, Prometeo alterano il turnflow)
        //Invio la notifica al giocatore
        //Mi metto in attesa della risposta


    }

    @Override
    protected void stateEnd() {


    }
}
