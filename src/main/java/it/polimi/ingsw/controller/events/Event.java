package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.view.ViewEventHandler;
import it.polimi.ingsw.view.serverView.VirtualView;

import java.io.Serializable;

/**
 * This interface is used to implement the Visitor pattern.
 * Every specific event overrides the visit() functions it's going to use and implements Event
 */
public interface Event extends Serializable {

    default void visit(LoginController eventHandler, VirtualView view) {
        view.showMessage("Operation not permitted!");
    }

    default void visit(ChallengerSelectionController eventHandler, VirtualView view) {
        view.showMessage("Operation not permitted!");
    }

    default void visit(ViewEventHandler eventHandler) {
        return;
    } //TODO Add second parameter "view" to events

    default void visit(GodPowerController eventHandler, VirtualView view) {
        view.showMessage("Operation not permitted");
    }

    default void visit(WorkerSetupController eventHandler, VirtualView view) {
        view.showMessage("Operation not permitted!");
    }

    default void visit(GamePhasesController eventHandler, VirtualView view) {
        view.showMessage("Operation not permitted!");
    }
}
