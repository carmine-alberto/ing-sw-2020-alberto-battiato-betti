package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.ViewState;

import java.net.Socket;

public class VirtualChallengerSelectionViewState extends VirtualViewStateState {

    public VirtualChallengerSelectionViewState() {};

    public VirtualChallengerSelectionViewState(VirtualView virtualView, Socket clientSocket, ViewState viewState) {
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
