package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

public class GameEndUpdate implements Event {
    public String winnerNickname;

    public GameEndUpdate(String winnerNickname) {
        this.winnerNickname = winnerNickname;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }


}
