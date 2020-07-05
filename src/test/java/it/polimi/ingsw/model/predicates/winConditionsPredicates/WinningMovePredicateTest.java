package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class WinningMovePredicateTest {
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
        try {
            game.assignSelectedGodPowerToPlayer("Hestia" , player.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        worker = new GameWorker(game , player);
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game , i , j);

        cellToTest = board[0][0];
        predicate = new NotOnPerimeterPredicate();
    }

    @Test
    void test(){
        worker.setPosition(cellToTest);
        assertFalse(predicate.test(game, worker));
        worker.move(game.getCell(1 ,1));
        assertTrue(predicate.test(game , worker));
    }
}