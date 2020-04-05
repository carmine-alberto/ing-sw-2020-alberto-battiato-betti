package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class Controller {
    private Game currentGame;
    ControllerState controllerState;

    public Controller(Game game) {
        this.currentGame = game;
        this.controllerState = new LoginController(this);
    }

    public synchronized void handle(Event event, VirtualView view) {
        controllerState.handle(event, view);
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
