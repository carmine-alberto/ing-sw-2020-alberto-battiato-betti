package it.polimi.ingsw.controller;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.serverView.VirtualChallengerSelectionView;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.view.serverView.VirtualWaitingView;

import java.util.stream.Collectors;

public class LoginController extends ControllerState {
    public LoginController(Controller mainController) {
        super(mainController);
    }

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
        //This method will be called by not-expected Events only - a specific handler method is provided for the remaining events
    }

    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        Player newPlayer = new Player(loginEvent.playerUsername, senderView);

        mainController.getCurrentGame().addPlayer(newPlayer);
        newPlayer.getPlayerView().setNextState(new VirtualChallengerSelectionView());
        mainController.controllerState = new ChallengerSelectionController(mainController);

        System.out.println(mainController.getCurrentGame().getPlayers().stream().map(player -> player.getNickname()).collect(Collectors.toList()));

        Server.acceptNextPlayer = true; //TODO Remove when a better method to handle asynchronous connections is implemented

    }
}
