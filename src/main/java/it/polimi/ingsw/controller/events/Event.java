package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.io.Serializable;

public interface Event extends Serializable {
    void visit(LoginController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler);

    void visit(ViewEventHandler eventHandler);

    void visit(GodPowerController eventHandler);

    void visit(WorkerSetupController eventHandler, VirtualView view);

    void visit(GamePhasesController eventHandler, VirtualView view);
}
