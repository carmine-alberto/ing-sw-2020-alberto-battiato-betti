package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.ViewState;

import java.net.Socket;

public class VirtualGodPowerViewState extends VirtualViewStateState {

    public VirtualGodPowerViewState() {};

    public VirtualGodPowerViewState(VirtualView virtualView, Socket clientSocket, ViewState viewState) {
        super(virtualView, clientSocket, viewState);
    }
    @Override
    public void render() {
        //TODO throw exception
    }

    @Override
    public String toString() {
        return "GodPowerViewState";
    }
}
