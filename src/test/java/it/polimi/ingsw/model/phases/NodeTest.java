package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.predicates.actionPredicate.CanMovePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Node node , child;
    String phase;
    BiPredicate phasePredicate;

    @BeforeEach
    void setUp() {
        phase = "ChooseWorkerPhase";
        phasePredicate = new CanMovePredicate();
        node = new Node(null , phase , phasePredicate);
        child = new Node(node , "ChooseActionPhase " , new CanMovePredicate());
    }

    @Test
    void addChild() {
        assertTrue(node.getChildren().isEmpty());
        node.addChild(child);
        assertTrue(node.getChildren().contains(child));
    }

    @Test
    void getPhase() {
        assertEquals(phase , node.getPhase());
    }

    @Test
    void getPhasePredicate() {
        assertEquals(phasePredicate , node.getPhasePredicate());
    }
}