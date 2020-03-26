package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.MaxHeightReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldCellTest {
    FieldCell notOnPerimeterCell;

    @BeforeEach
    void setUp() {
        notOnPerimeterCell = new FieldCell(1,3);
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
        assertTrue(notOnPerimeterCell.isOnPerimeter());
    }

    @Test
    void isFree() {
    }

    @Test
    void getWorker() {
    }
}