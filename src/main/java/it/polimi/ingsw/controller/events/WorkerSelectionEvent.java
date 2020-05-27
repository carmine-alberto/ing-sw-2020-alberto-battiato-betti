package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
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
    public void visit(WorkerSetupController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

    @Override
    public void visit(GamePhasesController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }
}
