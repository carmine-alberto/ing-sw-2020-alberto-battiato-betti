package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class GamePhasesController extends ControllerState {

    public GamePhasesController(Controller mainController, Game currentGame) {
        super(mainController, currentGame);
    }


    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    /**
     * This is the specific handle function used to handle the userInputEvent
     *
     * @param event The userInputEvent
     * @param senderView The turnPlayer's view
     */
    public void handle(UserInputEvent event, VirtualView senderView) {
        try {
            if (isTurnPlayer(senderView))
                currentGame.runPhase(event.inputString);
            else
                senderView.showMessage("It's not your turn!");
        } catch (Exception e) {
            senderView.showMessage(e.getMessage());
        }

    }


}
