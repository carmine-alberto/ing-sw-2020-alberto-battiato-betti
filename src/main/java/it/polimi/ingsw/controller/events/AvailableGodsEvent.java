package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;

import java.util.List;

public class AvailableGodsEvent implements Event {
    public List<String> godPowers;

    public AvailableGodsEvent(List<String> godPowers) {
        this.godPowers = godPowers;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
