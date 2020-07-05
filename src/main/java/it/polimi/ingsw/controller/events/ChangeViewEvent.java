package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.view.ViewEventHandler;

public class ChangeViewEvent implements Event {
    public String viewState;

    public ChangeViewEvent(String viewState) {
        this.viewState = viewState;
    }

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
