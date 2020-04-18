package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorkerTest {

    GameWorker notOnPerimeterGameWorker, onPerimeterGameWorker;
    Game myGame;
    Player myPlayer;

    @BeforeEach
    void setUp() {
        myGame = new Game();
        myPlayer = new Player("Franco");
        notOnPerimeterGameWorker = new GameWorker(new Move(), new Build(), myGame, myPlayer);
        notOnPerimeterGameWorker.setPosition(myGame.getCell(2, 3));
        onPerimeterGameWorker = new GameWorker(new Move(), new Build(), myGame, myPlayer);
        onPerimeterGameWorker.setPosition(myGame.getCell(0, 0));
    }

    @Test
    void move() {
        notOnPerimeterGameWorker.move(2, 4);
        assertEquals(myGame.getCell(2, 4) ,notOnPerimeterGameWorker.getCell());
    }
}