package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.View;

import java.net.Socket;

public class VirtualChallengerSelectionView extends View {

    public VirtualChallengerSelectionView() {};

    public VirtualChallengerSelectionView(VirtualView virtualView, Socket clientSocket, View viewState) {
        super(virtualView, clientSocket, viewState);
    }

    @Override
    public String toString() {
        return "ChallengerSelectionView";
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }
}
