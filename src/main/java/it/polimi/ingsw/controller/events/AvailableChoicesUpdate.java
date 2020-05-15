package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;

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
