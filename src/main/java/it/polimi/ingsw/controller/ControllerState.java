package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.view.clientView.View;
import it.polimi.ingsw.view.serverView.VirtualView;


public abstract class ControllerState {
    protected Controller mainController;

    public ControllerState(Controller mainController) {
        this.mainController = mainController;
    }


    public abstract void handle(Event event, VirtualView view);
}
