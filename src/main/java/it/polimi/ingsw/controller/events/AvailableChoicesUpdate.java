package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

import java.util.List;

public class AvailableChoicesUpdate implements Event {
    public List<String> availableChoices;
    public AvailableChoicesUpdate(List<String> availableChoices) {
        this.availableChoices = availableChoices;
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
