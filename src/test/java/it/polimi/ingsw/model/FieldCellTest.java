package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldCellTest {
    Game myGame;
    FieldCell notOnPerimeterCell;
    FieldCell onPerimeterCell;
    FieldCell notFreeCell;

    @BeforeEach
    void setUp() {
        myGame = new Game();
        notOnPerimeterCell = new FieldCell(myGame, 2, 3);
        onPerimeterCell = new FieldCell(myGame, 0, 0);

        notFreeCell = new FieldCell(myGame, 3, 3);
        notFreeCell.placeDome();
    }

    @Test
    void incrementHeight() {
        notOnPerimeterCell.incrementHeight();
        assertEquals(1, (int) notOnPerimeterCell.getHeight());
    }

    @Test
    void placeDome() {
        notOnPerimeterCell.placeDome();
        assertTrue(notOnPerimeterCell.isComplete());
    }

    @Test
    void isOnPerimeter() {
        assertFalse(notOnPerimeterCell.isOnPerimeter());
        assertTrue(onPerimeterCell.isOnPerimeter());
    }

    @Test
    void isFree() {
        assertFalse(notFreeCell.isFree());
    }

    @Test
    void getAdjacentCells() {
        List<FieldCell> expectedAvailable = List.of(myGame.getCell(0,1), myGame.getCell(1,0),
                myGame.getCell(1,1));

        assertEquals(expectedAvailable, onPerimeterCell.getAdjacentCells());

        expectedAvailable = List.of(myGame.getCell(1 , 2), myGame.getCell(1 , 3), myGame.getCell(1 , 4),
                                    myGame.getCell(2 , 2),                               myGame.getCell(2 , 4),
                                    myGame.getCell(3 , 2), myGame.getCell(3 , 3), myGame.getCell(3 , 4)
        );
        assertEquals(expectedAvailable, notOnPerimeterCell.getAdjacentCells());
    }

}