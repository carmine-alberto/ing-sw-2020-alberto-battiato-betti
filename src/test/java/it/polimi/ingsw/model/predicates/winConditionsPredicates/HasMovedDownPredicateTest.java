package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HasMovedDownPredicateTest {
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
        game.getPlayers().add(player);
        game.initGods();
        try {
            game.assignSelectedGodPowerToPlayer("Pan" , player.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        worker = new GameWorker(game , player);
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game , i , j);

        cellToTest = board[1][1];
        cellToTest.incrementHeight();
        board[0][0].incrementHeight();
        board[0][0].incrementHeight();

        game.setTurnPlayer(player);
        worker.setPosition(board[0][0]);
        player.getPlayerState().setSelectedCell(cellToTest);
        predicate = new HasMovedDownPredicate(2);
    }

    @Test
    void test(){
        assertFalse(predicate.test(game , worker));
        player.getPlayerState().setSelectedCell(board[1][0]);
        assertTrue(predicate.test(game , worker));
    }
}