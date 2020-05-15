package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class GameStartedEvent implements Event {

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);

    }
}
