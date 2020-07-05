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
        try {
            game.addPlayer(first.getNickname(), null);
            first.setCurrentGame(game);

            game.addPlayer(second.getNickname(), null);
            second.setCurrentGame(game);

            game.addPlayer(third.getNickname(), null);
            third.setCurrentGame(game);
            firstOpponents.add(second);
            firstOpponents.add(third);
            secondOpponents.add(first);
            secondOpponents.add(third);
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getOpponents() {
        assertEquals(firstOpponents, first.getOpponents());
        assertEquals(secondOpponents, second.getOpponents());
    }
}