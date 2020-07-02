package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.PingEvent;
import it.polimi.ingsw.cview.serverView.VirtualView;


public abstract class ControllerState {
    protected Controller mainController;

    public ControllerState(Controller mainController) {
        this.mainController = mainController;
    }

    /**
     * This function just sends the event calling its visit function
     *
     * @param event The event the controller has to handle
     * @param view The current view
     */
    public abstract void handle(Event event, VirtualView view);

    /**
     * This function is used to understand if the player is the turnPlayer
     *
     * @param view the view of the player you want to know about
     * @return true if he's the turnPlayer, false otherwise
     */
    protected Boolean isTurnPlayer(VirtualView view) {
        return mainController.getCurrentGame().getTurnPlayer().getPlayerView().equals(view);
    }

    /**
     * This function handles the ping event
     * @param event the ping sent
     * @param view the view that is now running
     */
    public void handle(PingEvent event, VirtualView view) {
        view.resetTimerFlag();
    }
}
