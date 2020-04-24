package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;

public class GamePhasesController extends ControllerState {
    Player turnPlayer;
    Game currentGame;

    public GamePhasesController(Controller mainController) {
        super(mainController);
        currentGame = mainController.getCurrentGame();
    }

    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(UserInputEvent event, VirtualView view) {
        try {
            turnPlayer = currentGame.getTurnPlayer();

            if (turnPlayer.getPlayerView().equals(view)) {
                currentGame.runPhase(event.inputString);
            }
            else
                view.showMessage("It's not your turn!");
        } catch (Exception e) {
            turnPlayer.getPlayerView().showMessage(e.getMessage());
        }

    }


}
