package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.view.serverView.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.List;

import static it.polimi.ingsw.GameSettings.FIRST_PLAYER_INDEX;
import static it.polimi.ingsw.GameSettings.SECOND_PLAYER_INDEX;

public class GodPowerController extends ControllerState {
    private Integer choosingPlayerIndex;
    private static final Integer GODPOWER_LEFT_INDEX = 0;

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public GodPowerController(Controller mainController, Game currentGame) {
        super(mainController, currentGame);
        choosingPlayerIndex = SECOND_PLAYER_INDEX;
        //The player selecting the first godPower is always the second to join the game
    }

    public void handle(GodSelectionEvent event, VirtualView senderView) {
        String choosingPlayerName = currentGame.getNthPlayer(choosingPlayerIndex);

        if (senderView.getOwnerName().equals(choosingPlayerName))
            try {
                List<String> godPowersList = currentGame.getGodPowers();

                currentGame.assignSelectedGodPowerToPlayer(event.selectedGod, choosingPlayerName);
                currentGame.removeGodPowerFromAvailableGods(event.selectedGod);
                godPowersList.remove(event.selectedGod);    //Looks redundant? Necessary for the below code to work (if condition evaluates to true)

                senderView.changeViewState(new VirtualWaitingViewState(senderView));
                choosingPlayerIndex++;

                if (onlyOneGodPowerLeft(godPowersList)) {
                    currentGame.assignSelectedGodPowerToPlayer(godPowersList.get(GODPOWER_LEFT_INDEX), currentGame.getNthPlayer(FIRST_PLAYER_INDEX));
                    currentGame.removeGodPowerFromAvailableGods(godPowersList.get(GODPOWER_LEFT_INDEX));
                    moveToNextState();
                } else {
                    choosingPlayerName = currentGame.getNthPlayer(choosingPlayerIndex);
                    controller.getViewByOwner(choosingPlayerName).changeViewState(new VirtualGodPowerViewState(controller.getViewByOwner(choosingPlayerName)));
                }
            } catch (InvalidSelectionException e) {
                senderView.showMessage("Invalid selection, try again");
            }
    }

    private Boolean onlyOneGodPowerLeft(List<String> godPowersList) {
        return godPowersList.size() == 1;
    }

    private void moveToNextState() {
        controller
                .getViews()
                .stream()
                .filter(view -> !view.getOwnerName().equals(currentGame.getTurnPlayerNickname()))
                .forEach(view -> view.changeViewState(new VirtualWaitingViewState(view)));

        VirtualView turnPlayerView = controller.getViewByOwner(currentGame.getTurnPlayerNickname());
        turnPlayerView.changeViewState(new VirtualWorkerSetupViewState(turnPlayerView));

        controller.next(new WorkerSetupController(controller, currentGame));
    }
}
