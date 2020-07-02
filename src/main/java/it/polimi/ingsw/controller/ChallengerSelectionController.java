package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.cview.serverView.VirtualGodPowerViewState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.cview.serverView.VirtualWaitingViewState;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.stream.Collectors;

import static it.polimi.ingsw.GameSettings.*;

public class ChallengerSelectionController extends ControllerState {
    public ChallengerSelectionController(Controller mainController) {
        super(mainController);
        notifyAvailableGods(mainController.getCurrentGame().getPlayers().get(0));
    }

    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    /**
     * This is the specific handle function used to handle the loginEvent sent by the other player (someone else is now connected)
     * @param loginEvent
     * @param senderView
     */
    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        Game currentGame = mainController.getCurrentGame();
        Player newPlayer;

        try {
            if (currentGame.NUM_OF_PLAYERS == -1 && currentGame.getPlayers().size() < TWO  || currentGame.getPlayers().size() < currentGame.NUM_OF_PLAYERS) { //If the challenger has not chosen yet
                newPlayer = new Player(loginEvent.playerUsername, senderView);
                currentGame.addPlayer(newPlayer);
                newPlayer.getPlayerView().changeView(new VirtualWaitingViewState());
            } else {
                senderView.showMessage("Challenger not ready! Try again later");
                senderView.terminate();
            }
            if (currentGame.getPlayers().size() == currentGame.NUM_OF_PLAYERS)
                moveToNextState(currentGame);
        } catch (InvalidSelectionException e) {
            senderView.showMessage(e.getMessage());
        }

        System.out.println(mainController.getCurrentGame().getPlayers().stream().map(player -> player.getNickname()).collect(Collectors.toList()));

    }

    /**
     * This is the specific handle function used to handle the challengerSelectionEvent
     *
     * @param event The challengerSelectionEvent
     * @param senderView
     */
    public void handle(ChallengerSelectionEvent event, VirtualView senderView) { //TODO Check legality of choices
        mainController.getCurrentGame().setCurrentPlayerIndex(event.selectedStartingPlayerIndex - CORRECTION);
        mainController.getCurrentGame().NUM_OF_PLAYERS = event.selectedNumberOfPlayers;
        mainController.getCurrentGame().setGodPowers(event.selectedGods);

        mainController.getCurrentGame().getPlayers().get(FIRST_PLAYER_INDEX).getPlayerView().changeView(new VirtualWaitingViewState());

        if (mainController.getCurrentGame().getPlayers().size() == mainController.getCurrentGame().NUM_OF_PLAYERS) {
            moveToNextState(mainController.getCurrentGame());
        }

    }

    private void moveToNextState(Game currentGame) {
        currentGame.getPlayers().get(FIRST_PLAYER_INDEX).getPlayerView().changeView(new VirtualWaitingViewState());
        currentGame.getPlayers().get(SECOND_PLAYER_INDEX).getPlayerView().changeView(new VirtualGodPowerViewState());
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
