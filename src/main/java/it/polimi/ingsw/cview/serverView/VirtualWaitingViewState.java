package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.ViewState;

import java.net.Socket;

public class VirtualWaitingViewState extends VirtualViewStateState {

    public VirtualWaitingViewState() {}; //TODO Refactor constructors in case attributes are never used
    public VirtualWaitingViewState(VirtualView virtualView, Socket clientSocket, ViewState viewState) {
        super(virtualView, clientSocket, viewState);
    }

    @Override
    public String toString() {
        return "WaitingViewState";
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }
}
