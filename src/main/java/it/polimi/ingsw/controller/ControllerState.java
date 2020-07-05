package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.ClientDisconnectedEvent;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.PingEvent;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.model.Game;


public abstract class ControllerState {
    protected Controller controller;
    protected Game currentGame;

    public ControllerState(Controller mainController, Game currentGame) {
        this.controller = mainController;
        this.currentGame = currentGame;
    }

    /**
     * This function just sends the event calling its visit function
     *
     * @param event The event the controller has to handle
     * @param view The view in which the player is now
     */
    public abstract void handle(Event event, VirtualView view);

    public void handle(ClientDisconnectedEvent event, VirtualView view) {
        controller.handleViewDisconnection(view);
    }

    public void handle(PingEvent event, VirtualView view) {
        view.resetTimerFlag();
    }

    protected Boolean isTurnPlayer(VirtualView view) {
        return currentGame.isTurnPlayer(view.getOwnerName());
    }
}
