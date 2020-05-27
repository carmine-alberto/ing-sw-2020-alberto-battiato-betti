package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Constructible.BLOCK;
import static it.polimi.ingsw.model.Constructible.DOME;
import static org.junit.jupiter.api.Assertions.*;

class BuildTest {

    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    GameWorker worker;
    Player player;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        player = new Player("Niub", null);
        worker = new GameWorker(new Move(), new Build(), game, player);

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game, i, j);
    }

    @Test
    void runDome() {
        worker.build(board[2][2], DOME);
        assertEquals(board[2][2].getHeight(), Integer.valueOf(0));
        assertEquals(board[2][2].getHasDome(), true);
    }

    @Test
    void runNOoDome() {
        worker.build(board[2][2], BLOCK);
        assertEquals(board[2][2].getHeight(), Integer.valueOf(1));
        assertEquals(board[2][2].getHasDome(), false);
        worker.build(board[2][2], BLOCK);
        assertEquals(board[2][2].getHeight(), Integer.valueOf(2));
    }
}