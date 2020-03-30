package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ControllerState;

import java.io.Serializable;

public class LoginEvent implements Event {
    public String playerUsername;

    public LoginEvent(String username) {
        this.playerUsername = username;
    }

    @Override
    public void visit(ControllerState eventHandler) {
        eventHandler.handle(this);
    }
}
