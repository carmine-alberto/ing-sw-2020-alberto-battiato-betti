package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.view.serverView.VirtualGodPowerViewState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.view.serverView.VirtualWaitingViewState;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import static it.polimi.ingsw.utility.GameSettings.*;

public class ChallengerSelectionController extends ControllerState {

    public ChallengerSelectionController(Controller mainController, Game currentGame) {
        super(mainController, currentGame);
    }

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    /**
     * This is the specific handle function used to handle the LoginEvent sent by the other players (someone else is now connected)
     * @param loginEvent The received event
     * @param senderView The view sending the event
     */
    public void handle(LoginEvent loginEvent, VirtualView senderView) {
        try {
            if (!currentGame.isChallenger(senderView.getOwnerName())) {
                if (currentGame.hasFreeSlots()) {
                    currentGame.addPlayer(loginEvent.playerUsername, senderView);
                    senderView.setOwner(loginEvent.playerUsername);
                    currentGame.addObserver(senderView);
                    controller.handleConnectedView(senderView);

                    senderView.changeViewState(new VirtualWaitingViewState(senderView));
                } else {
                    senderView.showMessage("Challenger not ready! Try again later");
                    senderView.terminate();
                }

                if (currentGame.haveAllPlayersConnected()) {
                    moveToNextState(currentGame);
                }
            }
        } catch (InvalidSelectionException e) {
            senderView.showMessage(e.getMessage());
        }
    }

    /**
     * This is the specific handle function used to handle the challengerSelectionEvent
     *
     * @param event The challengerSelectionEvent
     * @param senderView The view sending the event
     */
    public void handle(ChallengerSelectionEvent event, VirtualView senderView) {
        try {
            if (currentGame.isChallenger(senderView.getOwnerName())) {
                currentGame.registerChallengerSelection(event.selectedStartingPlayerIndex,
                        event.selectedNumberOfPlayers,
                        event.selectedGods);

                senderView.changeViewState(new VirtualWaitingViewState(senderView));

                if (currentGame.haveAllPlayersConnected())
                    moveToNextState(currentGame);
            }
        } catch (InvalidSelectionException e) {
            senderView.showMessage(e.getMessage());
        }
    }

    private void moveToNextState(Game currentGame) {
        VirtualView nthPlayerView = controller.getViewByOwner(currentGame.getNthPlayer(SECOND_ELEMENT_INDEX));
        nthPlayerView.changeViewState(new VirtualGodPowerViewState(nthPlayerView));
        controller.next(new GodPowerController(controller, currentGame));
    }

}
