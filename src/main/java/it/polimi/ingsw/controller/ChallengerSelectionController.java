package it.polimi.ingsw.controller;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.cview.clientView.GodPowerView;
import it.polimi.ingsw.cview.serverView.VirtualGodPowerView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.cview.serverView.VirtualWaitingView;
import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.stream.Collectors;

public class ChallengerSelectionController extends ControllerState {
    public ChallengerSelectionController(Controller mainController) {
        super(mainController);
        notifyAvailableGods(mainController.getCurrentGame().getPlayers().get(0));
    }

    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        Game currentGame = mainController.getCurrentGame();
        Player newPlayer;

        try {
            if (currentGame.NUM_OF_PLAYERS == -1 && currentGame.getPlayers().size() < 2 || currentGame.getPlayers().size() < currentGame.NUM_OF_PLAYERS) { //If the challenger has not chosen yet
                newPlayer = new Player(loginEvent.playerUsername, senderView);
                currentGame.addPlayer(newPlayer);
                newPlayer.getPlayerView().changeView(new VirtualWaitingView());
            } else {
                senderView.showMessage("Challenger not ready! Try again later");
                senderView.terminate();
            }
            if (currentGame.getPlayers().size() == currentGame.NUM_OF_PLAYERS)
                moveToNextState(currentGame);
        } catch (InvalidSelectionException e) {
            senderView.showMessage(e.getMessage());
            senderView.terminate();
        }

        System.out.println(mainController.getCurrentGame().getPlayers().stream().map(player -> player.getNickname()).collect(Collectors.toList()));

    }

    public void handle(ChallengerSelectionEvent event, VirtualView senderView) { //TODO Check legality of choices
        mainController.getCurrentGame().setCurrentPlayerIndex(event.selectedStartingPlayerIndex - 1);
        mainController.getCurrentGame().NUM_OF_PLAYERS = event.selectedNumberOfPlayers;
        mainController.getCurrentGame().setGodPowers(event.selectedGods);

        mainController.getCurrentGame().getPlayers().get(0).getPlayerView().changeView(new VirtualWaitingView());

        if (mainController.getCurrentGame().getPlayers().size() == mainController.getCurrentGame().NUM_OF_PLAYERS) {
            moveToNextState(mainController.getCurrentGame());
        }

    }

    private void moveToNextState(Game currentGame) {
        currentGame.getPlayers().get(0).getPlayerView().changeView(new VirtualWaitingView());
        currentGame.getPlayers().get(1).getPlayerView().changeView(new VirtualGodPowerView());
        mainController.controllerState = new GodPowerController(mainController);
    }

    private void notifyAvailableGods(Player playerToBeNotified) {
        playerToBeNotified
                .getPlayerView()
                .sendToClient(new AvailableGodsEvent(mainController
                        .getCurrentGame()
                        .getGodPowers()
                        .stream()
                        .collect(Collectors.toList()))); //This may be moved into the model: the game changes (god powers are added) and a notification is sent - Downsides: what if the gods are set before other clients are connected?
    }
}
