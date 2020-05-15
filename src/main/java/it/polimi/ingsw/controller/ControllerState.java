package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.serverView.VirtualView;


public abstract class ControllerState {
    protected Controller mainController;

    public ControllerState(Controller mainController) {
        this.mainController = mainController;
    }


    public abstract void handle(Event event, VirtualView view);

    protected Boolean isTurnPlayer(VirtualView view) {
        return mainController.getCurrentGame().getTurnPlayer().getPlayerView().equals(view);
    }

    /*public void handle(PingEvent event, VirtualView view) {
        //reset timer
    }*/
}
