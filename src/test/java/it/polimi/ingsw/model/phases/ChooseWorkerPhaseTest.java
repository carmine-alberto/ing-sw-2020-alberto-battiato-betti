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

class ChooseWorkerPhaseTest {
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
        game.assignSelectedGodPowerToPlayer("Apollo" , player);
        second = new Player("Pollo" , null);
        second.setCurrentGame(game);
        game.getPlayers().add(second);
        game.assignSelectedGodPowerToPlayer("Artemis" , second);
        third = new Player("Tacchino" , null);
        third.setCurrentGame(game);
        game.getPlayers().add(third);
        game.assignSelectedGodPowerToPlayer("Athena" , third);

        firstWorker = new GameWorker(game, player);
        workers = new ArrayList<>();
        workers.add(firstWorker);
        player.setWorkers(workers);

        secondWorker = new GameWorker(game , second);

        game.setTurnPlayer(player);
        firstWorker.setPosition(game.getCell(1 , 1));
        secondWorker.setPosition(game.getCell(2 , 2));
        player.getPlayerState().setSelectedWorker(firstWorker);
        player.getPlayerState().setSelectedCell(game.getCell(1 , 2));
        game.setCurrentPlayerIndex(0);
    }

    @Test
    void run() throws IllegalFormatException, InvalidSelectionException {
        phase = new ChooseWorkerPhase(game , null);
        assertThrows(InvalidSelectionException.class , () -> phase.run("4 4"));
        assertThrows(InvalidSelectionException.class , () -> phase.run("3 3"));
        phase.run("2 2");
        assertEquals(firstWorker , player.getPlayerState().getSelectedWorker());
    }
}