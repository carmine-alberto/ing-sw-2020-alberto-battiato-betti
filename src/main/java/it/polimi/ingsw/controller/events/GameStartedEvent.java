package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

public class GameStartedEvent implements Event {

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);

    }
}
