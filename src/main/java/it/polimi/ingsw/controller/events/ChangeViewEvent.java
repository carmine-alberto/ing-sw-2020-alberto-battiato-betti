package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.view.serverView.ServerViewState;
import it.polimi.ingsw.view.serverView.VirtualView;

public class ChangeViewEvent implements Event {
    public ServerViewState nextState;

    public ChangeViewEvent(ServerViewState nextState) {
        this.nextState = nextState;
    }

    @Override
    public void visit(LoginController eventHandler, VirtualView view) {
        return;
    }

    @Override
    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {

    }

    @Override
    public void visit(ChallengerSelectionController eventHandler) {
        return;
    }
}
