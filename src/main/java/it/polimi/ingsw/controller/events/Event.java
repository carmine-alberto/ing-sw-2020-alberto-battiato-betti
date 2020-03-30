package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.ControllerState;

import java.io.Serializable;

public interface Event extends Serializable {
    public void visit(ControllerState eventHandler);
}
