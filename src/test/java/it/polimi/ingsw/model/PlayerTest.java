package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    Game game = new Game();
    Player first = new Player("Scroto" , null);
    Player second = new Player("Pallido" , null);
    Player third = new Player("Francesco" , null);
    List<Player> firstOpponents = new ArrayList<>();
    List<Player> secondOpponents = new ArrayList<>();

    @BeforeEach
    void setUp() {
        game.getPlayers().add(first);
        game.getPlayers().add(second);
        game.getPlayers().add(third);

        first.setCurrentGame(game);
        second.setCurrentGame(game);
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