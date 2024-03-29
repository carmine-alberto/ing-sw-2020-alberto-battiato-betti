package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.view.serverView.VirtualView;

public class LoginEvent implements Event {
    public String playerUsername;

    public LoginEvent(String username) {
        this.playerUsername = username;
    }


    @Override
    public void visit(LoginController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);

    }

    @Override
    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }


}
