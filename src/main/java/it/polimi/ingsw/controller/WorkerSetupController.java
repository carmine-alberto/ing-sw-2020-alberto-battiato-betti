package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class WorkerSetupController extends ControllerState {

    public WorkerSetupController(Controller mainController) {
        super(mainController);
        mainController.getCurrentGame().getTurnPlayer().getPlayerView().showMessage("Select your colour and the workers' starting position");
    }

    @Override
    public void handle(Event event, VirtualView view) {
        event.visit(this);
    }
}
