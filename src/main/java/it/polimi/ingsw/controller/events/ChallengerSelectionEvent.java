package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.GodPowerController;
import it.polimi.ingsw.controller.LoginController;
import it.polimi.ingsw.controller.WorkerSetupController;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

import java.util.List;

public class ChallengerSelectionEvent implements Event {
    public List<String> selectedGods;
    public Integer selectedNumberOfPlayers;
    public Integer selectedStartingPlayerIndex;


    public ChallengerSelectionEvent(Integer selectedNumberOfPlayers, List<String> selectedGods, Integer selectedStartingPlayerIndex) {
        this.selectedGods = selectedGods;
        this.selectedNumberOfPlayers = selectedNumberOfPlayers;
        this.selectedStartingPlayerIndex = selectedStartingPlayerIndex;
    }

    @Override
    public void visit(LoginController eventHandler, VirtualView view) {

    }

    @Override
    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

    @Override
    public void visit(ChallengerSelectionController eventHandler) {

    }

    @Override
    public void visit(ViewEventHandler eventHandler) {

    }

    @Override
    public void visit(GodPowerController eventHandler) {

    }

    @Override
    public void visit(WorkerSetupController eventHandler, VirtualView view) {

    }
}
