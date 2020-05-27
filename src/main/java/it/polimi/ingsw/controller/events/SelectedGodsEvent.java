package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;

import java.util.List;

public class SelectedGodsEvent implements Event {
    public List<String> godPowers;

    public SelectedGodsEvent(List<String> godPowers) {
        this.godPowers = godPowers;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }


}
