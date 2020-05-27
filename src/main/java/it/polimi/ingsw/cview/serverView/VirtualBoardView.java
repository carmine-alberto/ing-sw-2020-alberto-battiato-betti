package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.controller.events.BoardUpdate;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;

import java.net.Socket;

public class VirtualBoardView extends VirtualViewState implements Observer<Event> {  //TODO The view components should observe the game components (board, godPowers)

    public VirtualBoardView() {};

    public VirtualBoardView(VirtualView virtualView, Game currentGame) {
        this.virtualView = virtualView;
        currentGame.addObserver(this); //TODO Observers should be deregistered as well
    }

    @Override
    public String toString() {
        return "BoardView";
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
