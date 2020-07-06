package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameWorkerTest {

    GameWorker notOnPerimeterGameWorker, onPerimeterGameWorker;
    Game myGame;
    Player myPlayer;

    @BeforeEach
    void setUp() {
        myGame = new Game();
        myPlayer = new Player("Franco" , null);
        myGame.initGods();
        myGame.getPlayers().add(myPlayer);
        try {
            myGame.assignSelectedGodPowerToPlayer("Apollo" , myPlayer.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        notOnPerimeterGameWorker = new GameWorker(myGame, myPlayer);
        notOnPerimeterGameWorker.setPosition(myGame.getCell(2, 3));
        onPerimeterGameWorker = new GameWorker(myGame, myPlayer);
        onPerimeterGameWorker.setPosition(myGame.getCell(0, 0));
    }

    @Test
    void move() {
        notOnPerimeterGameWorker.move(myGame.getCell(2 , 4));
        assertEquals(myGame.getCell(2, 4) ,notOnPerimeterGameWorker.getCell());
    }
}