package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.View;

import java.net.Socket;

public class VirtualGodPowerView extends VirtualViewState {

    public VirtualGodPowerView() {};

    public VirtualGodPowerView(VirtualView virtualView, Socket clientSocket, View viewState) {
        super(virtualView, clientSocket, viewState);
    }
    @Override
    public void render() {
        //TODO throw exception
    }

    @Override
    public String toString() {
        return "GodPowerView";
    }
}
