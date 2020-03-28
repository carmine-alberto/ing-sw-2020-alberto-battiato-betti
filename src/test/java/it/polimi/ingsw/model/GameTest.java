package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;
    Player player1;
    Player player2;

    @BeforeEach
    void setUp(){
        game = new Game();
        game.addPlayer(player1);
        game.addPlayer(player2);
    }

    @Test
    void getPlayers() {

    }

    @Test
    void setTurnPlayer() {
    }

    @Test
    void getCell() {
    }

    @Test
    void addPlayer() {
    }
}