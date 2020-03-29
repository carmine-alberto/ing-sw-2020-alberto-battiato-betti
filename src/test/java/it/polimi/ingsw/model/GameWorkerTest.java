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
<<<<<<< HEAD
        List<FieldCell> expectedAvailable = List.of(new FieldCell(1,2), new FieldCell(1,3), new FieldCell(1,4),
                                                    new FieldCell(2,2),                                  new FieldCell(2,4),
                                                    new FieldCell(3,2), new FieldCell(3,3), new FieldCell(3,4));
        assertEquals(expectedAvailable, normalGameWorker.getAdjacentCells());
=======
        List<FieldCell> expectedAvailable = List.of(gioco.getCell(0,1), gioco.getCell(1,0),
                                                    gioco.getCell(1,1));

        assertEquals(expectedAvailable, onPerimeterGameWorker.getAdjacentCells());

        expectedAvailable = List.of(gioco.getCell(1 , 2), gioco.getCell(1 , 3), gioco.getCell(1 , 4),
                                    gioco.getCell(2 , 2),                             gioco.getCell(2 , 4),
                                    gioco.getCell(3 , 2), gioco.getCell(3 , 3), gioco.getCell(3 , 4)
                );
        assertEquals(expectedAvailable, notOnPerimeterGameWorker.getAdjacentCells());
>>>>>>> 24c8d7d61ffbf9ee7c6a06c4670b9ab4e55e7412
    }

    @Test
    void move() {
        notOnPerimeterGameWorker.move(2, 4);
        assertEquals(gioco.getCell(2, 4) ,notOnPerimeterGameWorker.getCell());
    }
}