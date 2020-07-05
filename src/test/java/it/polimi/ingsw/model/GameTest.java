package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.GameSettings.FIRST_PLAYER_INDEX;
import static it.polimi.ingsw.GameSettings.THIRD_PLAYER_INDEX;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;
    Player player1;
    Player player2;
    Player player3;

    @BeforeEach
    void setUp() {
        game = new Game();
        player1 = new Player("Es1" , null);

        player2 = new Player("Es2" , null);

        player3 = new Player("Es3" , null);

        try {
            game.addPlayer(player1.getNickname(), null);
            game.assignSelectedGodPowerToPlayer("Apollo" , player1.getNickname());
            game.addPlayer(player2.getNickname() , null);
            game.assignSelectedGodPowerToPlayer("Artemis" , player2.getNickname());
            game.addPlayer(player3.getNickname() , null);
            game.assignSelectedGodPowerToPlayer("Athena" , player3.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }

        List<GameWorker> workers = new ArrayList<>();
        player1.setWorkers(workers);
    }

    @Test
    void addPlayer() {
        Player alreadyExistingPlayer = new Player("Es3" , null);

        List<Player> allPlayers = List.of(player1, player2, player3);

        Assertions.assertThrows(InvalidSelectionException.class, () -> game.addPlayer(alreadyExistingPlayer.getNickname() , null));

        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void removeTurnPlayer() {
        game.setCurrentPlayerIndex(FIRST_PLAYER_INDEX);
        game.setTurnPlayer(player1);
        game.initGame();
        game.removeTurnPlayer();
        List<Player> allPlayers = List.of(player2, player3);
        assertEquals(allPlayers, game.getPlayers());
    }

    @Test
    void removeGodPowerFromAvailableGods() {
        assertEquals(14 , game.getGodPowers().size());
        game.removeGodPowerFromAvailableGods("Apollo");
        assertEquals(13 , game.getGodPowers().size());
    }

    @Test
    void assignSelectedGodPowerToPlayer(){
        try {
            game.assignSelectedGodPowerToPlayer("Athena" , player3.getNickname());
            assertEquals("Athena" , player3.getSelectedGod().getName());
            game.assignSelectedGodPowerToPlayer("Artemis" , player3.getNickname());
            assertEquals("Artemis" , player3.getSelectedGod().getName());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void initGods(){
        Game g = new Game();
        assertNull(game.getGodPowers());
        game.initGods();
        assertNotNull(game.getGodPowers());
    }
    @Test
    void setNextTurnPlayer() {
        game.setCurrentPlayerIndex(THIRD_PLAYER_INDEX);
        game.setTurnPlayer(player3);
        game.setNextTurnPlayer();
        assertEquals(player1, game.getTurnPlayer());
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
    void hasFreeSlotsTest(){
        List<String> godPowers = new ArrayList<>();
        godPowers.add("Athena");
        godPowers.add("Artemis");
        try {
            game.registerChallengerSelection(0 , 2 , godPowers);
            assertFalse(game.hasFreeSlots());
            assertFalse(game.haveAllPlayersConnected());
            godPowers.add("Prometheus");
            game.registerChallengerSelection(0 , 3 , godPowers);
            assertTrue(game.haveAllPlayersConnected());
            godPowers.add("Pan");
            game.registerChallengerSelection(0 , 4 , godPowers);
            assertTrue(game.hasFreeSlots());
            godPowers.clear();
            assertThrows(InvalidSelectionException.class , () -> game.registerChallengerSelection(1 , 0 , godPowers));
            godPowers.add("Pan");
            assertThrows(InvalidSelectionException.class , () -> game.registerChallengerSelection(0 , 3 , godPowers));
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }
    }
}