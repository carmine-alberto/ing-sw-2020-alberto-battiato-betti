package it.polimi.ingsw.model.predicates.constructiblePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static it.polimi.ingsw.model.Constructible.BLOCK;
import static it.polimi.ingsw.model.Constructible.DOME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockPredicateTest {
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
        player.getPlayerState().setSelectedCell(cellToTest);
        predicate = new BlockPredicate(3);
    }

    @Test
    void test(){
        assertFalse(predicate.test(player , DOME));
        assertTrue(predicate.test(player , BLOCK));
    }
}