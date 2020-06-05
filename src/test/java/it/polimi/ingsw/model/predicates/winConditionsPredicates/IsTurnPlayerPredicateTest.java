package it.polimi.ingsw.model.predicates.winConditionsPredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class IsTurnPlayerPredicateTest {
    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    Player firstPlayer, secondPlayer;
    GameWorker firstWorker, secondWorker;
    FieldCell cellToTest;
    BiPredicate predicate;

    @BeforeEach
    void setUp(){
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        firstPlayer = new Player("Zio" , null);
        game.assignSelectedGodPowerToPlayer("Apollo", firstPlayer);
        firstWorker = new GameWorker( game , firstPlayer);
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game , i , j);

        secondPlayer = new Player("lol" , null);
        game.assignSelectedGodPowerToPlayer("Artemis", secondPlayer);
        secondWorker = new GameWorker(game , secondPlayer);
        game.setTurnPlayer(firstPlayer);
        predicate = new IsTurnPlayerPredicate();
    }

    @Test
    void test(){
        assertFalse(predicate.test(game , secondWorker));
        assertTrue(predicate.test(game , firstWorker));
    }
}