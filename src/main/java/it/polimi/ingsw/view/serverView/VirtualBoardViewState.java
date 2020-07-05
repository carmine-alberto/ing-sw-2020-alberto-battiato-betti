package it.polimi.ingsw.view.serverView;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.controller.events.Event;

public class VirtualBoardViewState extends VirtualViewState implements Observer<Event> {

    public VirtualBoardViewState(VirtualView virtualView) {
        super(virtualView);
    }


    public VirtualView getVirtualView() {
        return virtualView;
    }

}
