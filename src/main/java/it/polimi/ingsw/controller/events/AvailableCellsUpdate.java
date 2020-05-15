package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.cview.clientView.ViewEventHandler;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.FieldCell;

import java.util.List;
import java.util.stream.Collectors;

public class AvailableCellsUpdate implements Event {
    public List<Integer> xCoordinates;
    public List<Integer> yCoordinates;

    public AvailableCellsUpdate(List<FieldCell> availableCells) {
        xCoordinates = availableCells.stream().map(cell -> cell.getPosX()).collect(Collectors.toList());
        yCoordinates = availableCells.stream().map(cell -> cell.getPosY()).collect(Collectors.toList());
    }


    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
