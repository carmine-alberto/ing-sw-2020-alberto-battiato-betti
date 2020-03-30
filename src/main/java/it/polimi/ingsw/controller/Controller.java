package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.model.Game;

public class Controller {
    private Game currentGame;
    private ControllerState controllerState;

    public Controller(Game game) {
        this.currentGame = game;
        this.controllerState = new LoginController(this);
    }

    public void handle(Event event) {
        event.visit(controllerState);
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
