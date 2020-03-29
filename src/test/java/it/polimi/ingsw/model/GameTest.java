package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import it.polimi.ingsw.view.LoginView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;
    Player player1;
    Player player2;
    Player player3;

    @BeforeEach
    void setUp() {
        game = new Game();
        player1 = new Player("Es1");
        player2 = new Player("Es2");
        player3 = new Player("Es3");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
    }

    @Test
    void getPlayers() {
        List<Player> allPlayers = List.of(player1, player2, player3);
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void setTurnPlayer() {
        game.setTurnPlayer(player1);
        assertEquals(player1, game.getTurnPlayer());
    }

    @Test
    void addPlayer() {
        Player alreadyExistingPlayer = new Player("Es3");

        List<Player> allPlayers = List.of(player1, player2, player3);
        game.addPlayer(player3);
        game.addPlayer(alreadyExistingPlayer);
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void removeTurnPlayer() {
        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player2);
        game.removeTurnPlayer();
        List<Player> allPlayers = List.of(player2, player3);
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void setNextTurnPlayer() {
        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player3);
        game.setNextTurnPlayer();
        assertEquals(player1, game.getTurnPlayer());

        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player3);
        game.removeTurnPlayer();
        assertEquals(player1, game.getTurnPlayer());
    }


    @Test
    void endGame() {
        Integer a = 2+2;
        return;
    }

}