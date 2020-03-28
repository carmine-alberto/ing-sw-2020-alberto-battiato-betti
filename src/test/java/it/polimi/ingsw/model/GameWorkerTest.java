package it.polimi.ingsw.model;

import it.polimi.ingsw.actions.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorkerTest {

    GameWorker normalGameWorker;
    Game gioco;

    @BeforeEach
    void setUp() {
        gioco = new Game(); //TODO completare inizializzazione
        normalGameWorker = new GameWorker(new Move(), gioco);
        normalGameWorker.setPosition(gioco.getCell(2, 3));
    }

    @Test
    void getAvailableCells() {
        List<FieldCell> expectedAvailable = List.of(new FieldCell(1,2), new FieldCell(1,3), new FieldCell(1,4),
                                                    new FieldCell(2,2),                                 new FieldCell(2,4),
                                                    new FieldCell(3,2), new FieldCell(3,3), new FieldCell(3,4));
        assertEquals(expectedAvailable, normalGameWorker.getAdjacentCells());
    }

    @Test
    void move() {
        normalGameWorker.move(2, 4);
        assertEquals(gioco.getCell(2, 4) ,normalGameWorker.getCell());
    }
}