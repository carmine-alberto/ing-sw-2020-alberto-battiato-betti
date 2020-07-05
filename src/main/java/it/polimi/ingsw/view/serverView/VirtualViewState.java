package it.polimi.ingsw.view.serverView;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.view.ViewState;

import java.util.List;

public abstract class VirtualViewState extends ViewState {

    public VirtualViewState() {}

    public VirtualViewState(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    @Override
    protected void connectionClosedHandler() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    @Override
    public void showMessage(String message) {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    @Override
    public void showWarning(String message) {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    /**
     *
     * @param availableChoices You want to be shown
     */
    @Override
    public void showChoices(List<String> availableChoices) {
        throw new UnsupportedOperationException("Operation not available on the server");
    }

    public void handleUpdate(Event message) {
        virtualView.sendToClient(message);
    }

    @Override
    public void terminate(String reason) {
        virtualView.terminate();
    }

    public String toString() {
        return this.getClass().getName().replace(this.getClass().getPackageName() + ".Virtual", "");
    }
}
