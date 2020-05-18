package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class PingEvent implements Event {

    public void visit(LoginController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
   }

    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

    public void visit(ChallengerSelectionController eventHandler) {
        visit(eventHandler, null);
    }

    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
     }                                                      //TODO Add second parameter "view" to events

    public void visit(GodPowerController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
     }

    public void visit(WorkerSetupController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

    public void visit(GamePhasesController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }
}
