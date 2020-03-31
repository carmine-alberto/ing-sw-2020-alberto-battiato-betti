package it.polimi.ingsw.controller;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.serverView.VirtualChallengerSelectionView;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.view.serverView.VirtualWaitingView;

import java.util.stream.Collectors;

public class ChallengerSelectionController extends ControllerState {
    public ChallengerSelectionController(Controller mainController) {
        super(mainController);
    }

    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        Player newPlayer = new Player(loginEvent.playerUsername, senderView);
        mainController.getCurrentGame().addPlayer(newPlayer);
        newPlayer.getPlayerView().setNextState(new VirtualWaitingView());
        Server.acceptNextPlayer = true; //TODO Remove when a better method to handle asynchronous connections is implemented

        System.out.println(mainController.getCurrentGame().getPlayers().stream().map(player -> player.getNickname()).collect(Collectors.toList()));

    }

    public void handle(ChallengerSelectionEvent event) { //TODO Check legality of choices
        mainController.getCurrentGame().setCurrentPlayerIndex(event.selectedStartingPlayerIndex - 1);
        mainController.getCurrentGame().NUM_OF_PLAYERS = event.selectedNumberOfPlayers;

        Server.acceptNextPlayer = true; //TODO Remove when a better method to handle asynchronous connections is implemented

        if (mainController.getCurrentGame().getPlayers().size() == mainController.getCurrentGame().NUM_OF_PLAYERS) {
            //TODO Implement phase swap
        }

        //TODO Add selected God Powers to the game
    }
}
