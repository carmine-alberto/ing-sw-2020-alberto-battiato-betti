package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.controller.events.SelectedGodsEvent;
import it.polimi.ingsw.cview.serverView.VirtualBoardView;
import it.polimi.ingsw.cview.serverView.VirtualGodPowerView;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.cview.serverView.VirtualWaitingView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GodPowerController extends ControllerState {
    private Integer choosingPlayerIndex;

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this);
    }


    public GodPowerController(Controller mainController) {
        super(mainController);
        choosingPlayerIndex = 1; //The player selecting the first godPower is always the second to join the game
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
            choosingPlayer.getPlayerView().changeView(new VirtualWaitingView());
            choosingPlayerIndex++;
            if (choosingPlayerIndex % currentGame.NUM_OF_PLAYERS == 0) {
                currentGame.assignSelectedGodPowerToPlayer(godPowersList.get(0), currentGame.getPlayers().get(0));
                currentGame.removeGodPowerFromAvailableGods(godPowersList.get(0));
                moveToNextState();
            } else {
                choosingPlayer = currentGame.getPlayers().get(choosingPlayerIndex);
                choosingPlayer.getPlayerView().changeView(new VirtualGodPowerView());
                notifySelectedGods(currentGame.getPlayers().get(choosingPlayerIndex));
            }
        } else
            choosingPlayer.getPlayerView().showMessage("Invalid selection, try again");

    }

    private void moveToNextState() {
        mainController.getCurrentGame().getPlayers().forEach(player -> player.getPlayerView().changeView(new VirtualBoardView(player.getPlayerView(), mainController.getCurrentGame())));
        mainController.controllerState = new WorkerSetupController(mainController);
    }
}
