package it.polimi.ingsw.controller;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.cview.serverView.VirtualChallengerSelectionView;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.util.stream.Collectors;

public class LoginController extends ControllerState {
    public LoginController(Controller mainController) {
        super(mainController);
    }

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        Player newPlayer = new Player(loginEvent.playerUsername, senderView);

        mainController.getCurrentGame().addPlayer(newPlayer);
        newPlayer.getPlayerView().changeView(new VirtualChallengerSelectionView());
        mainController.controllerState = new ChallengerSelectionController(mainController);

        System.out.println(mainController.getCurrentGame().getPlayers().stream().map(player -> player.getNickname()).collect(Collectors.toList()));

    }
}
