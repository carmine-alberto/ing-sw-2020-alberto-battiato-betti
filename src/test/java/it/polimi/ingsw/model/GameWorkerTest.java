package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorkerTest {

    GameWorker notOnPerimeterGameWorker, onPerimeterGameWorker;
    Game gioco;

    @BeforeEach
    void setUp() {
        gioco = new Game();
        notOnPerimeterGameWorker = new GameWorker(new Move(), gioco);
        notOnPerimeterGameWorker.setPosition(gioco.getCell(2, 3));
        onPerimeterGameWorker = new GameWorker(new Move(), gioco);
        onPerimeterGameWorker.setPosition(gioco.getCell(0, 0));
    }

    @Test
    void getAvailableCells() {
        List<FieldCell> expectedAvailable = List.of(gioco.getCell(0,1), gioco.getCell(1,0),
                                                    gioco.getCell(1,1));

        assertEquals(expectedAvailable, onPerimeterGameWorker.getCell().getAdjacentCells());

        expectedAvailable = List.of(gioco.getCell(1 , 2), gioco.getCell(1 , 3), gioco.getCell(1 , 4),
                                    gioco.getCell(2 , 2),                             gioco.getCell(2 , 4),
                                    gioco.getCell(3 , 2), gioco.getCell(3 , 3), gioco.getCell(3 , 4)
                );
        assertEquals(expectedAvailable, notOnPerimeterGameWorker.getCell().getAdjacentCells());

    }

    @Test
    void move() {
        notOnPerimeterGameWorker.move(2, 4);
        assertEquals(gioco.getCell(2, 4) ,notOnPerimeterGameWorker.getCell());
    }
}