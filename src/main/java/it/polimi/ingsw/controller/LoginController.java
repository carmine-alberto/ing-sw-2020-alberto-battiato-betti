package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.model.Player;

public class LoginController extends ControllerState {
    public LoginController(Controller mainController) {
        super(mainController);
    }

    @Override
    public void handle(Event event) {
        //This method will be called by not-expected Events only - a specific handler method is provided for the remaining events
    }

    public void handle(LoginEvent loginEvent) {
        mainController.getCurrentGame().addPlayer(new Player(loginEvent.playerUsername));

    }
}
