package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.controller.WorkerSetupController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class ChangeViewEvent implements Event {
    public String viewState;

    public ChangeViewEvent(String nextState) {
        this.viewState = nextState;
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

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);

    }

    @Override
    public void visit(GodPowerController eventHandler) {

    }

    @Override
    public void visit(WorkerSetupController eventHandler, VirtualView view) {

    }
}
