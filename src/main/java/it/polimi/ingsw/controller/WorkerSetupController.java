package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;

import java.util.ArrayList;
import java.util.List;

public class WorkerSetupController extends ControllerState {

    public WorkerSetupController(Controller mainController) {
        super(mainController);
        promptTurnPlayer();
    }


    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(WorkerSelectionEvent workerSelectionEvent, VirtualView view) {
        Player turnPlayer = mainController.getCurrentGame().getTurnPlayer();
        List<GameWorker> playerWorkers = new ArrayList<>(); //TODO Call Player builder

        if (view.equals(turnPlayer.getPlayerView())) {

            turnPlayer.setColour(workerSelectionEvent.selectedColor);

            for (Integer i = 0; i < 2; i++) {
                playerWorkers.add(new GameWorker(new Move(), new Build(), mainController.getCurrentGame(), turnPlayer));
                playerWorkers.get(i).setPosition(mainController.getCurrentGame().getCell(workerSelectionEvent.xCoordinates.get(i) - 1, //TODO Check bounds: exceptions or checks?
                        workerSelectionEvent.yCoordinates.get(i) - 1));
            }
            turnPlayer.setWorkers(playerWorkers);

            mainController.getCurrentGame().setNextTurnPlayer();
            promptTurnPlayer();

        }
    }

    private void promptTurnPlayer() {
        mainController
                .getCurrentGame()
                .getTurnPlayer()
                .getPlayerView()
                .showMessage("Select your colour and the workers' starting position");
    }
}
