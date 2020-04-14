package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.controller.WorkerSetupController;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class UserInputEvent implements Event {
    public String dest;

    public UserInputEvent(String dest) {
        this.dest = dest;
    }

    @Override
    public void visit(LoginController eventHandler, VirtualView view) {

    }

    @Override
    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {

    }

    @Override
    public void visit(ChallengerSelectionController eventHandler) {

    }

    @Override
    public void visit(ViewEventHandler eventHandler) {

    }

    @Override
    public void visit(GodPowerController eventHandler) {

    }

    @Override
    public void visit(WorkerSetupController eventHandler, VirtualView view) {

    }

}
