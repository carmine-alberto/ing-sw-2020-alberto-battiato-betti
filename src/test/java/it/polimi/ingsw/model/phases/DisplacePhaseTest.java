package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisplacePhaseTest {
    Game game;
    Player player , second , third;
    GameWorker firstWorker , secondWorker;
    List<GameWorker> workers;
    TurnPhase phase;
    InvalidSelectionException e;

    @BeforeEach
    void setUp() {
        game = new Game();
        player = new Player("Giovanni", null);
        player.setCurrentGame(game);
        game.getPlayers().add(player);
        second = new Player("Paolo" , null);
        second.setCurrentGame(game);
        game.getPlayers().add(second);
        third = new Player("secondo" , null);
        third.setCurrentGame(game);
        game.getPlayers().add(third);
        game.initGods();
        try {
            game.assignSelectedGodPowerToPlayer("Apollo" , player.getNickname());
            game.assignSelectedGodPowerToPlayer("Artemis" , second.getNickname());
            game.assignSelectedGodPowerToPlayer("Athena" , third.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }

        firstWorker = new GameWorker(game, player);
        workers = new ArrayList<>();
        workers.add(firstWorker);
        player.setWorkers(workers);
        secondWorker = new GameWorker(game , second);

        game.setTurnPlayer(player);
        firstWorker.setPosition(game.getCell(1 , 1));
        player.getPlayerState().setSelectedWorker(firstWorker);
        secondWorker.setPosition(game.getCell(2 , 2));
        phase = new DisplacePhase(game , null);
        game.setCurrentPlayerIndex(0);
    }

    @Test
    void stateInit() {
        phase.stateInit();
        assertEquals(3 , game.getPlayers().size());
        firstWorker.move(game.getCell(0 , 0));
        phase.stateInit();
        assertEquals(2 , game.getPlayers().size());
    }

    @Test
    void run() throws IllegalFormatException, InvalidSelectionException {
        phase.stateInit();
        assertThrows(InvalidSelectionException.class , () -> phase.run("1 1"));
        phase.run("3 3");
        assertEquals(game.getCell(0 , 0) , secondWorker.getCell());
    }
}