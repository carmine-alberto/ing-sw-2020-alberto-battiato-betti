package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.ControllerState;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.view.serverView.VirtualView;

import java.io.Serializable;

public interface Event extends Serializable {
    void visit(LoginController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler);
}
