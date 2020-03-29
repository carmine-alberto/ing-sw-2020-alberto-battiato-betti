package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Game game = new Game();
    Player first = new Player("Scroto");
    Player second = new Player("Pallido");
    Player third = new Player("Francesco");
    List<Player> firstOpponents = new ArrayList<>();
    List<Player> secondOpponents = new ArrayList<>();

    @BeforeEach
    void setUp() {
        game.addPlayer(first);
        first.setCurrentGame(game);
        game.addPlayer(second);
        second.setCurrentGame(game);
        game.addPlayer(third);
        third.setCurrentGame(game);
        firstOpponents.add(second);
        firstOpponents.add(third);
        secondOpponents.add(first);
        secondOpponents.add(third);
    }

    @Test
    void getOpponents() {
        assertEquals(firstOpponents, first.getOpponents());
        assertEquals(secondOpponents, second.getOpponents());
    }
}