package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

import java.util.List;

public class SelectedGodsUpdate implements Event {
    public List<String> godPowers;

    public SelectedGodsUpdate(List<String> godPowers) {
        this.godPowers = godPowers;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }


}
