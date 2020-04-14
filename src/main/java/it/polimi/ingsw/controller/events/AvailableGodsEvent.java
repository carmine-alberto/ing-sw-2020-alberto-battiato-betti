package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.util.List;

public class AvailableGodsEvent implements Event {
    public List<String> godPowers;

    public AvailableGodsEvent(List<String> godPowers) {
        this.godPowers = godPowers;
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
