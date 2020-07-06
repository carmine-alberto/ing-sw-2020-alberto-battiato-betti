package it.polimi.ingsw.model.predicates;

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

class DisplacePredicateTest {
    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    Player player , secondPlayer;
    GameWorker worker , secondWorker;
    FieldCell cellToTest;
    BiPredicate predicate;

    @BeforeEach
    void setUp(){
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        player = new Player("Zio" , null);
        player.setCurrentGame(game);
        game.getPlayers().add(player);
        game.initGods();
        try {
            game.assignSelectedGodPowerToPlayer("Charon" , player.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        worker = new GameWorker(game , player);
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game , i , j);

        worker.setPosition(board[0][0]);
        board[0][0].setOccupyingWorker(worker);
        secondPlayer = new Player("lol" , null);
        secondPlayer.setCurrentGame(game);
        game.getPlayers().add(secondPlayer);
        game.initGods();
        try {
            game.assignSelectedGodPowerToPlayer("Artemis", secondPlayer.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        secondWorker = new GameWorker(game , secondPlayer);
        cellToTest = board[1][1];
        secondWorker.setPosition(cellToTest);
        cellToTest.setOccupyingWorker(secondWorker);
        predicate = new DisplacePredicate();
    }

    @Test
    void test(){
        assertFalse(predicate.test(cellToTest , worker));
        cellToTest = board[2][2];
        secondWorker.move(cellToTest);
        assertFalse(predicate.test(cellToTest , worker));
        worker.move(board[1][1]);
        assertTrue(predicate.test(cellToTest , worker));
    }
}