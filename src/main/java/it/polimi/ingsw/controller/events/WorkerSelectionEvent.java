package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.controller.WorkerSetupController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.util.List;

public class WorkerSelectionEvent implements Event {
    public List<Integer> xCoordinates;
    public List<Integer> yCoordinates;
    public String selectedColor;

    public WorkerSelectionEvent(List<Integer> xCoordinates, List<Integer> yCoordinates, String color) {
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
        this.selectedColor = color;
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
        eventHandler.handle(this, view);
    }
}