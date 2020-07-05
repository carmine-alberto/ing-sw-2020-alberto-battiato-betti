package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsCellFreePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildPhaseTest {
    Game game;
    Player player , second , third;
    GameWorker worker;
    List<GameWorker> workers;
    TurnPhase phase;
    InvalidSelectionException e;

    @BeforeEach
    void setUp() {
        game = new Game();
        player = new Player("Gianpiero", null);
        player.setCurrentGame(game);
        game.getPlayers().add(player);
        try {
            game.assignSelectedGodPowerToPlayer("Apollo" , player.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }
        second = new Player("Angela" , null);
        second.setCurrentGame(game);
        game.getPlayers().add(second);
        try {
            game.assignSelectedGodPowerToPlayer("Artemis" , second.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }
        worker = new GameWorker(game, player);
        workers = new ArrayList<>();
        workers.add(worker);
        player.setWorkers(workers);
        third = new Player("Gioele" , null);
        third.setCurrentGame(game);
        game.getPlayers().add(third);
        try {
            game.assignSelectedGodPowerToPlayer("Athena" , third.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }

        game.setTurnPlayer(player);
        worker.setPosition(game.getCell(1 , 1));
        player.getPlayerState().setSelectedWorker(worker);
        phase = new BuildPhase(game , new IsCellFreePredicate());
        game.setCurrentPlayerIndex(0);
    }

    @Test
    void stateInit() {
        phase.stateInit();
        assertEquals(3 , game.getPlayers().size());
        worker.move(game.getCell(0 , 0));
        game.getCell(0 , 1).placeDome();
        game.getCell(1 , 0).placeDome();
        game.getCell(1 , 1).placeDome();
        phase.stateInit();
        assertEquals(2 , game.getPlayers().size());
    }

    @Test
    void run() throws IllegalFormatException, InvalidSelectionException {
        phase.stateInit();
        phase.run("1 1");
        assertEquals(game.getCell(0 , 0) , player.getPlayerState().getSelectedCell());
        assertThrows(InvalidSelectionException.class , () -> phase.run("2 2"));
    }
}