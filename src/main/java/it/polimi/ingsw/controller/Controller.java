package it.polimi.ingsw.controller;

import it.polimi.ingsw.utility.Observer;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.serverView.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller implemented using the State pattern: event handling is delegated to the substates
 */
public class Controller implements Observer<Event> {
    private Game currentGame;
    private List<VirtualView> connectedViews;
    private ControllerState controllerState;

    public Controller(Game game) {
        this.currentGame = game;
        this.controllerState = new LoginController(this, game);
        this.connectedViews = new ArrayList<>();
    }

    @Override
    public synchronized <V> void update(Event event, V virtualView) {
        controllerState.handle(event, (VirtualView) virtualView);
    }

    /**
     * This method has to be called by the object creating the View for it to be set up properly inside the Controller
     * @param connectedView The newly-created view
     */
    public void handleConnectedView(VirtualView connectedView) {
        this.connectedViews.add(connectedView);
    }

    VirtualView getViewByOwner(String owner) {
        return connectedViews
                .stream()
                .filter(view -> view.getOwnerName().equals(owner))
                .findFirst()
                .get();
    }

    List<VirtualView> getViews() {
        return connectedViews;
    }

    void next(ControllerState nextState) {
        this.controllerState = nextState;
    }

    void handleViewDisconnection(VirtualView disconnectedView) {
        disconnectedView.removeObserver(this);

        connectedViews
                .stream()
                .filter(view -> !view.equals(disconnectedView))
                .forEach(this::notifyConnectedView);

        System.exit(0);
    }

    private void notifyConnectedView(VirtualView virtualView) {
        virtualView.showMessage(virtualView.getOwnerName() + " has disconnected and the game has been terminated.");
        virtualView.terminate();
        virtualView.removeObserver(this);
    }
}
