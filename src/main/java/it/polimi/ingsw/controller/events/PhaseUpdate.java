package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class PhaseUpdate implements Event {
    public String message;

    public PhaseUpdate(String message) {
        this.message = message;
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

    @Override
    public void visit(GamePhasesController eventHandler, VirtualView view) {

    }
}
