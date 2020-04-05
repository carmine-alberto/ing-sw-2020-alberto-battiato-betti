package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.io.Serializable;

public interface Event extends Serializable {
    void visit(LoginController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler, VirtualView view);

    void visit(ChallengerSelectionController eventHandler);

    void visit(ViewEventHandler eventHandler);

    void visit(GodPowerController godPowerController);
}
