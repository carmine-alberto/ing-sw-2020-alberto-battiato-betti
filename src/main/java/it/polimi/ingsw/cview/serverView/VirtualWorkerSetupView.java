package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.Game;

public class VirtualWorkerSetupView extends VirtualViewState implements Observer<Event> {

    public VirtualWorkerSetupView () {};

    public VirtualWorkerSetupView (VirtualView virtualView, Game currentGame) {
        this.virtualView = virtualView;
        currentGame.addObserver(this); //TODO Observers should be deregistered as well
    }

    @Override
    public String toString() {
        return "WorkerSetupView";
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }


    @Override
    public void update(Event message) {
        virtualView.sendToClient(message);
    }
}
