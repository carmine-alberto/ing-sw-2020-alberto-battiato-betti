package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;

public class WarningEvent implements Event {
    public String message;

    public WarningEvent(String messageContent) {
        this.message = messageContent;
    }

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }


}
