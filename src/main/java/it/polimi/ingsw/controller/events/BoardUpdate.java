package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.cview.ViewEventHandler;
import it.polimi.ingsw.model.FieldCell;

public class BoardUpdate implements Event {
    public FieldCell[][] board;

    public BoardUpdate(FieldCell[][] board) {
        this.board = board.clone();
    }

    @Override
    public void visit(ViewEventHandler eventHandler) {
        eventHandler.handle(this);
    }

}
