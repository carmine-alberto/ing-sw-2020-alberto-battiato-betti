package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldCellTest {
    FieldCell cella;

    @BeforeEach
    void setUp() {
        cella = new FieldCell(1,3);
    }

    @Test
    void incrementHeight() {
        cella.incrementHeight();
        assertEquals(1, (int)cella.getHeight());
    }

    @Test
    void placeDome() {
    }

    @Test
    void isOnPerimeter() {
        assertTrue(cella.isOnPerimeter());
    }

    @Test
    void isFree() {
    }

    @Test
    void getWorker() {
    }
}