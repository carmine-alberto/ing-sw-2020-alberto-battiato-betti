package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

import java.util.List;

public class AvailableGodsUpdate implements Event {
    public List<String> godPowers;

    public AvailableGodsUpdate(List<String> godPowers) {
        this.godPowers = godPowers;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
