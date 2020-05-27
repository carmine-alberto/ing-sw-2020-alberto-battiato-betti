package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
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
    public void visit(ChallengerSelectionController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

}
