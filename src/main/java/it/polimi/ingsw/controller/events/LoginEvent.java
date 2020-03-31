package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.ControllerState;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.view.serverView.VirtualView;

import java.io.Serializable;

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

    @Override
    public void visit(ChallengerSelectionController eventHandler) {

    }
}
