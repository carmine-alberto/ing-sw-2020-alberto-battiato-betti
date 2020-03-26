package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldCellTest {
    FieldCell notOnPerimeterCell;
    FieldCell onPerimeterCell;
    FieldCell notFreeCell;

    @BeforeEach
    void setUp() {
        notOnPerimeterCell = new FieldCell(2,3);
        onPerimeterCell = new FieldCell(5,5);

        notFreeCell = new FieldCell(3, 3);
        notFreeCell.placeDome();
    }

    @Test
    void incrementHeight() throws MaxHeightReachedException {
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

}