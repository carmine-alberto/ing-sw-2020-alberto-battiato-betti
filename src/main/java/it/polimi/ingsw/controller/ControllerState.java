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
     * This function is necessary for the Visitor to work - substates implement it calling the Event's generic visit() function
     *
     * @param event The event the controller has to handle
     * @param view The view sending the event
     */
    public abstract void handle(Event event, VirtualView view);

    public void handle(ClientDisconnectedEvent event, VirtualView view) {
        controller.handleViewDisconnection(view);
    }

    public void handle(PingEvent event, VirtualView view) {
        view.resetTimerFlag();
    }

    /**
     * This function is used to understand if the player is the turnPlayer
     *
     * @param view The view of the player you want to know about
     * @return true if they're the turnPlayer, false otherwise
     */
    protected Boolean isTurnPlayer(VirtualView view) {
        return currentGame.isTurnPlayer(view.getOwnerName());
    }
}
