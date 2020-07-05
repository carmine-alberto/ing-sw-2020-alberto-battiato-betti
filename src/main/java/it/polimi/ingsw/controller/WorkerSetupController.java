package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.BoardUpdate;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.view.serverView.VirtualBoardViewState;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.view.serverView.VirtualWorkerSetupViewState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

public class WorkerSetupController extends ControllerState {

    public WorkerSetupController(Controller mainController, Game currentGame) {
        super(mainController, currentGame);
        controller.getViewByOwner(currentGame.getTurnPlayerNickname()).sendToClient(new BoardUpdate(currentGame.getField()));
        promptTurnPlayer();
    }


    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(WorkerSelectionEvent workerSelectionEvent, VirtualView senderView) {
        String turnPlayerNickname = currentGame.getTurnPlayerNickname();

        if (isTurnPlayer(senderView)) {
            try {
                currentGame.handleColorAndWorkerSelection(workerSelectionEvent.selectedColor,
                        workerSelectionEvent.xCoordinates,
                        workerSelectionEvent.yCoordinates);

                currentGame.setNextTurnPlayer();
                turnPlayerNickname = currentGame.getTurnPlayerNickname();

                if (currentGame.isReadyToStart()) {
                    moveToNextState();
                } else {
                    VirtualView turnPlayerView = controller.getViewByOwner(turnPlayerNickname);

                    turnPlayerView.changeViewState(new VirtualWorkerSetupViewState(turnPlayerView));
                    turnPlayerView.sendToClient(new BoardUpdate(currentGame.getField()));
                    promptTurnPlayer();
                }
            } catch (InvalidSelectionException e) {
                senderView.showMessage(e.getMessage());
            }
        } else
            senderView.showMessage("It's not your turn!");
    }

    private void promptTurnPlayer() {
        controller
                .getViewByOwner(currentGame.getTurnPlayerNickname())
                .showMessage("Select your colour and the workers' starting position");
    }

    private void moveToNextState() {
        controller.getViews().forEach(view -> view.changeViewState(new VirtualBoardViewState(view)));
        controller.next(new GamePhasesController(controller, currentGame));
        currentGame.initGame();
    }
}
