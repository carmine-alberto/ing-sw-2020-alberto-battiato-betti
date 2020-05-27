package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;

public class PlayerLostUpdate implements Event {
    public String losingPlayerNickname;

    public PlayerLostUpdate(String nickname) {
        this.losingPlayerNickname = nickname;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);

    }

}
