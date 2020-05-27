package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class UserInputEvent implements Event {
    public String inputString;

    public UserInputEvent(String inputString) {
        this.inputString = inputString;
    }


    @Override
    public void visit(GamePhasesController eventHandler, VirtualView view) {
        eventHandler.handle(this, view);
    }

}
