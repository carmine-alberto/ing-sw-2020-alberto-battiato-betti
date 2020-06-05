package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class MaxHeightPredicateTest {
    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    Player player;
    GameWorker worker;
    FieldCell cellToTest;
    BiPredicate predicate;

    @BeforeEach
    void setUp(){
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        player = new Player("Zio" , null);
        game.assignSelectedGodPowerToPlayer("Hestia" , player);
        worker = new GameWorker(game , player);
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game , i , j);

        cellToTest = board[0][0];
        cellToTest.incrementHeight();
        predicate = new MaxHeightPredicate(0);
    }

    @Test
    void test(){
        assertFalse(predicate.test(cellToTest , worker));
        assertTrue(predicate.test(board[2][1] , worker));
    }
}