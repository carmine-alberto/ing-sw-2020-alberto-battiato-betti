package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveAndSetTest {

    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    GameWorker firstWorker;
    Player firstPlayer;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        firstPlayer = new Player("Grande", null);
        game.assignSelectedGodPowerToPlayer("Athena", firstPlayer);
        firstWorker = new GameWorker(game, firstPlayer);

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game, i, j);

        firstWorker.setPosition(board[2][2]);
    }

    @Test
    void test(){
        firstWorker.move(game.getCell(3 , 3));
        assertEquals(null , firstPlayer.getSelectedGod().getOuterPredicate("movePredicate"));

        board[2][2].incrementHeight();
        firstWorker.move(board[2][2]);
        assertTrue(firstPlayer.getSelectedGod().getOuterPredicate("movePredicate") != null);

        board[3][3].incrementHeight();
        firstWorker.move(board[3][3]);
        assertEquals(null , firstPlayer.getSelectedGod().getOuterPredicate("movePredicate"));

    }
}