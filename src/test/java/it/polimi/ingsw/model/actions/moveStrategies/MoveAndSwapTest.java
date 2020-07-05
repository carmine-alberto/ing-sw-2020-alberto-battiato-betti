package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveAndSwapTest {
    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    Player firstPlayer , secondPlayer;
    GameWorker firstWorker , secondWorker;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        firstPlayer = new Player("Grandi", null);
        firstPlayer.setCurrentGame(game);
        secondPlayer = new Player("Palle" , null);
        secondPlayer.setCurrentGame(game);
        try {
            game.assignSelectedGodPowerToPlayer("Artemis" , secondPlayer.getNickname());
            game.assignSelectedGodPowerToPlayer("Apollo", firstPlayer.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        firstWorker = new GameWorker(game, firstPlayer);
        secondWorker = new GameWorker(game , secondPlayer);

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game, i, j);

        firstWorker.setPosition(board[0][0]);
        board[0][0].setOccupyingWorker(firstWorker);
        secondWorker.setPosition(board[1][1]);
        board[1][1].setOccupyingWorker(secondWorker);
    }

    @Test
    void test(){
        firstWorker.move(game.getCell(1 , 0));
        assertEquals(board[1][1] , secondWorker.getCell());

        firstWorker.move(secondWorker.getCell());
        assertEquals(board[1][0] , secondWorker.getCell());

    }
}