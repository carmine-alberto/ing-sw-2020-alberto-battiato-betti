package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;
import it.polimi.ingsw.model.FieldCell;

import java.util.List;
import java.util.stream.Collectors;

public class AvailableCellsUpdate implements Event {
    public List<Integer> xCoordinates;
    public List<Integer> yCoordinates;

    /**
     * in the constructor the coordinates are mapped for every cell given
     * @param availableCells the available board cells
     */
    public AvailableCellsUpdate(List<FieldCell> availableCells) {
        xCoordinates = availableCells.stream().map(cell -> cell.getPosX()).collect(Collectors.toList());
        yCoordinates = availableCells.stream().map(cell -> cell.getPosY()).collect(Collectors.toList());
    }


    @Override
    /**
     *
     */
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
