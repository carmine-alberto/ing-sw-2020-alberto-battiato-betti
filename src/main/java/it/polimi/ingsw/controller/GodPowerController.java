package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.controller.events.SelectedGodsEvent;
import it.polimi.ingsw.cview.serverView.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

import static it.polimi.ingsw.GameSettings.FIRST_PLAYER_INDEX;
import static it.polimi.ingsw.GameSettings.SECOND_PLAYER_INDEX;

public class GodPowerController extends ControllerState {
    private Integer choosingPlayerIndex;

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }


    public GodPowerController(Controller mainController) {
        super(mainController);
        choosingPlayerIndex = SECOND_PLAYER_INDEX; //The player selecting the first godPower is always the second to join the game
        notifySelectedGods(mainController.getCurrentGame().getPlayers().get(choosingPlayerIndex));
    }

    private void notifySelectedGods(Player playerToBeNotified) {
        playerToBeNotified
                .getPlayerView()
                .sendToClient(new SelectedGodsEvent(mainController
                        .getCurrentGame()
                        .getGodPowers()
                )); //This may be moved into the model: the game changes (god powers are added) and a notification is sent - Downsides: what if the gods are set before other clients are connected?
    }


    public void handle(GodSelectionEvent event) {
        Game currentGame = mainController.getCurrentGame();
        List<String> godPowersList = currentGame.getGodPowers();
        Player choosingPlayer = currentGame.getPlayers().get(choosingPlayerIndex);

        if (godPowersList.contains(event.selectedGod)) {
            currentGame.assignSelectedGodPowerToPlayer(event.selectedGod, choosingPlayer);
            currentGame.removeGodPowerFromAvailableGods(event.selectedGod);
            godPowersList.remove(event.selectedGod);    //Looks redundant? Necessary for the below code to work (if condition evaluates to true)
            choosingPlayer.getPlayerView().changeView(new VirtualWaitingViewState());
            choosingPlayerIndex++;
            if (choosingPlayerIndex % currentGame.NUM_OF_PLAYERS == 0) {
                currentGame.assignSelectedGodPowerToPlayer(godPowersList.get(FIRST_PLAYER_INDEX), currentGame.getPlayers().get(FIRST_PLAYER_INDEX));
                currentGame.removeGodPowerFromAvailableGods(godPowersList.get(FIRST_PLAYER_INDEX));
                moveToNextState();
            } else {
                choosingPlayer = currentGame.getPlayers().get(choosingPlayerIndex);
                choosingPlayer.getPlayerView().changeView(new VirtualGodPowerViewState());
                notifySelectedGods(currentGame.getPlayers().get(choosingPlayerIndex));
            }
        } else
            choosingPlayer.getPlayerView().showMessage("Invalid selection, try again");

    }

    private void moveToNextState() {
        Player turnPlayer = mainController.getCurrentGame().getTurnPlayer();

        mainController
                .getCurrentGame()
                .getPlayers()
                .stream()
                .filter(player -> !player.equals(turnPlayer))
                .forEach(player -> player.getPlayerView().changeView(new VirtualWaitingViewState()));

        turnPlayer
                .getPlayerView()
                .changeView(new VirtualWorkerSetupViewState(turnPlayer.getPlayerView(), mainController.getCurrentGame()));
        mainController.controllerState = new WorkerSetupController(mainController);
    }
}
