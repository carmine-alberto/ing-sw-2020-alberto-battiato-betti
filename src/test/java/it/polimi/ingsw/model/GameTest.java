package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
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
        try {
            game.addPlayer(player1);
            game.addPlayer(player2);
            game.addPlayer(player3);
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
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
        try {
            game.addPlayer(alreadyExistingPlayer);
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void removeTurnPlayer() {
        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player3);
        game.removeTurnPlayer();
        List<Player> allPlayers = List.of(player1, player2);
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void setNextTurnPlayer() {
        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player3);
        game.setNextTurnPlayer();
        assertEquals(player1, game.getTurnPlayer());
    }

    @Test
    void setNextTurnPlayerAfterRemovingTheLast() {
        game.setCurrentPlayerIndex(2);
        game.setTurnPlayer(player3);
        game.removeTurnPlayer();
        game.setNextTurnPlayer();
        assertEquals(player1, game.getTurnPlayer());

    }

    @Test
    void setNextTurnPlayerAfterRemovingTheSecond() {
        game.setCurrentPlayerIndex(1);
        game.setTurnPlayer(player2);
        game.removeTurnPlayer();
        game.setNextTurnPlayer();
        assertEquals(player3, game.getTurnPlayer());
    }


    @Test
    void endGame() {
        Integer a = 2+2;
        return;
    }

}