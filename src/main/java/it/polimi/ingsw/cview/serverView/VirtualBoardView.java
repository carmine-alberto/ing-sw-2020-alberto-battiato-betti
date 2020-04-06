package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.cview.View;

public class VirtualBoardView extends View {  //TODO The view components should observe the game components (board, godPowers)

    public VirtualBoardView() {};

    @Override
    public String toString() {
        return "BoardView";
    }

    @Override
    public void render() {
        throw new UnsupportedOperationException("Operation not available on the server");
    }
}
