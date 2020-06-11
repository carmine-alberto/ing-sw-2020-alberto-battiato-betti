package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.View;

import java.net.Socket;
import java.util.List;

public abstract class VirtualViewState extends View {

    public VirtualViewState() {}

    public VirtualViewState(VirtualView virtualView, Socket clientSocket, View viewState) {
        super(virtualView, clientSocket, viewState);
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

    @Override
    public void terminate() {
        virtualView.terminate();
    }
}
