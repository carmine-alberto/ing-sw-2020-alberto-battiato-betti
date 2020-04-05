package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

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

    @Override
    public void visit(ViewEventHandler eventHandler) {

    }

    @Override
    public void visit(GodPowerController godPowerController) {

    }
}
