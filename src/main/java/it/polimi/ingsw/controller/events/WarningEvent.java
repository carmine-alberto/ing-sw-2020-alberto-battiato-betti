package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.controller.WorkerSetupController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class WarningEvent implements Event {
    public String message;

    public WarningEvent(String messageContent) {
        this.message = messageContent;
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
        eventHandler.handle(this);
    }

    @Override
    public void visit(GodPowerController eventHandler) {

    }

    @Override
    public void visit(WorkerSetupController eventHandler, VirtualView view) {

    }
}
