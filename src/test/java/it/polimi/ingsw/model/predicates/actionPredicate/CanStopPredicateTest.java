package it.polimi.ingsw.model.predicates.actionPredicate;

import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class CanStopPredicateTest {
    BiPredicate<ActionEnum, Player> predicate;
    Player player;
    @BeforeEach
    void setUp() {
        predicate = new CanStopPredicate();
        player = new Player("Filiberto" , null);
    }

    @Test
    void test1() {
        assertFalse(predicate.test(ActionEnum.MOVE , player));
        assertTrue(predicate.test(ActionEnum.END , player));
    }
}