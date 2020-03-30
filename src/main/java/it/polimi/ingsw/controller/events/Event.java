package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ControllerState;

public interface Event {
    public void visit(ControllerState eventHandler);
}
