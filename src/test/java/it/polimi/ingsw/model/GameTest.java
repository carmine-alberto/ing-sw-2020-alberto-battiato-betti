package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    Game game;
    Player player1;
    Player player2;
    Player player3;

    @BeforeEach
    void setUp() {
        game = new Game();
        player1 = new Player("Es1" , null);
        game.assignSelectedGodPowerToPlayer("Apollo" , player1);

        player2 = new Player("Es2" , null);
        game.assignSelectedGodPowerToPlayer("Artemis" , player2);

        player3 = new Player("Es3" , null);
        game.assignSelectedGodPowerToPlayer("Athena" , player3);

        List<GameWorker> workers = new ArrayList<>();
        player1.setWorkers(workers);

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
        Player alreadyExistingPlayer = new Player("Es3" , null);

        List<Player> allPlayers = List.of(player1, player2, player3);

        Assertions.assertThrows(InvalidSelectionException.class, () -> game.addPlayer(alreadyExistingPlayer));

        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void removeTurnPlayer() {
        game.setCurrentPlayerIndex(0);
        game.setTurnPlayer(player1);
        game.initGame();
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
    }

    @Test
    void endGame() {
        Integer a = 2+2;
        return;
    }

}