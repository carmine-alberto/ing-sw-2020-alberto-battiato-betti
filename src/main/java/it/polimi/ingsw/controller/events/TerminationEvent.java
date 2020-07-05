package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

public class TerminationEvent implements Event {
    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }
}
