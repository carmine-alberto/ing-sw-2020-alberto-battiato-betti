package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.serverView.VirtualChallengerSelectionViewState;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

public class LoginController extends ControllerState {
    public LoginController(Controller mainController, Game currentGame) {
        super(mainController, currentGame);
    }

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        try {
            currentGame.addPlayer(loginEvent.playerUsername, senderView);
            currentGame.addObserver(senderView);
            controller.handleConnectedView(senderView);

            currentGame.initGods();
            senderView.changeViewState(new VirtualChallengerSelectionViewState(senderView));

            controller.next(new ChallengerSelectionController(controller, currentGame));
        } catch (InvalidSelectionException e) {
            //This catch will never be run: the first player launching the client can't choose an already-existing name
        }
    }
}
