package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Constructible.BLOCK;
import static it.polimi.ingsw.model.Constructible.DOME;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        game.assignSelectedGodPowerToPlayer("Apollo" , player);
        worker = new GameWorker(game, player);

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game, i, j);
    }

    @Test
    void test() {
        worker.build(board[2][2], DOME);
        assertEquals(board[2][2].getHeight(), Integer.valueOf(0));
        assertEquals(board[2][2].getHasDome(), true);
        worker.build(board[1][1] , BLOCK);
        assertEquals((Integer)1 , board[1][1].getHeight());
    }

}