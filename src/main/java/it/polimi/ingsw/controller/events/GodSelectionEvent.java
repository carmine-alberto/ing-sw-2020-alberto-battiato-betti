package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.serverView.VirtualView;

public class GodSelectionEvent implements Event {
    public String selectedGod;

    public GodSelectionEvent(String selectedGod) {
        this.selectedGod = selectedGod;
    }


    @Override
    public void visit(GodPowerController eventHandler, VirtualView view) {
        eventHandler.handle(this);
    }

  }
