package it.polimi.ingsw.controller.events;

import java.io.Serializable;
import java.util.List;

public class ChallengerSelectionEvent implements Serializable {
    public List<String> selectedGods;
    public Integer selectedNumberOfPlayers;


    public ChallengerSelectionEvent(Integer selectedNumberOfPlayers, List<String> selectedGods) {
        this.selectedGods = selectedGods;
        this.selectedNumberOfPlayers = selectedNumberOfPlayers;
    }
}
