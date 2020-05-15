package it.polimi.ingsw.model.predicates.movePredicates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CanMoveBackPredicateTest {

    @BeforeEach
    void setUp() {
        Game game = new Game();
        Player first = new Player("Jonny");
        Player second = new Player("Il peloso");
        GameWorker fW = new GameWorker(game, first);
        GameWorker sW = new GameWorker(game, second);

    }

    @Test
    void test1() {
    }
}