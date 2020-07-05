package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameInformationEvent implements Event {
    public List<String> playersName = new ArrayList<>();
    public List<String> chosenGods = new ArrayList<>();
    public List<String> chosenColor = new ArrayList<>();

    public GameInformationEvent(List<Player> players) {

        players.forEach(
                player -> {
                    playersName.add(player.getNickname());
                    chosenGods.add(player.getSelectedGod().getName());
                    chosenColor.add(player.getColour());
                }
        );
    }

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }
}
