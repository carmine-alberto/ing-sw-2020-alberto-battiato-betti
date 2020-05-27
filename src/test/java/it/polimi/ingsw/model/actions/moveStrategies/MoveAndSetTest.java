package it.polimi.ingsw.model.actions.moveStrategies;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsDeltaHeightLessThanPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Constructible.BLOCK;
import static org.junit.jupiter.api.Assertions.*;

class MoveAndSetTest {

    static Integer FIELD_SIZE = 5;
    Game game;
    FieldCell[][] board;
    GameWorker workerMAS;
    GameWorker worker;
    Player playerMAS;
    Player player;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = new FieldCell[FIELD_SIZE][FIELD_SIZE];
        playerMAS = new Player("Grande", null);
        player = new Player("Niub", null);
        game.assignSelectedGodPowerToPlayer("Pan", player);
        game.assignSelectedGodPowerToPlayer("Athena", playerMAS);
        workerMAS = new GameWorker(new MoveAndSet(new IsDeltaHeightLessThanPredicate(1)), new Build(), game, playerMAS);
        worker = new GameWorker(new Move(), new Build(), game, player);

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                board[i][j] = new FieldCell(game, i, j);

        worker.build(board[2][2], BLOCK);
        worker.build(board[3][3], BLOCK);
    }

    @Test
    void movesUp(){
        worker.setPosition(board[3][2]);
        workerMAS.setPosition(board[2][1]);

        workerMAS.move(3,3);
        //assertEquals(new IsDeltaHeightLessThanPredicate(1), playerMAS.getSelectedGod().getOuterPredicate("movePredicate"));
        worker.move(4,4); //move does not check predicates (idiot)


        //assertEquals(Integer.valueOf(4), worker.getCell().getPosX());
        //assertEquals(Integer.valueOf(3), worker.getCell().getPosY());
    }
}