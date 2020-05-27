package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.View;

import java.net.Socket;

public class VirtualWaitingView extends VirtualViewState {

    public VirtualWaitingView() {}; //TODO Refactor constructors in case attributes are never used
    public VirtualWaitingView(VirtualView virtualView, Socket clientSocket, View viewState) {
        super(virtualView, clientSocket, viewState);
    }

    @Override
    public String toString() {
        return "WaitingView";
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }
}
