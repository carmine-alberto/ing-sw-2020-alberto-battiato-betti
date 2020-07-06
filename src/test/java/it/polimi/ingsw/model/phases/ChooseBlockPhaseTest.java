package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChooseBlockPhaseTest {
    Game game;
    Player player , second , third;
    GameWorker worker;
    List<GameWorker> workers;
    TurnPhase phase;
    InvalidSelectionException e;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.initGods();
        player = new Player("Giovanni", null);
        player.setCurrentGame(game);
        game.getPlayers().add(player);
        try {
            game.assignSelectedGodPowerToPlayer("Apollo" , player.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }
        second = new Player("Pollo" , null);
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
        third = new Player("Tacchino" , null);
        third.setCurrentGame(game);
        game.getPlayers().add(third);
        try {
            game.assignSelectedGodPowerToPlayer("Artemis" , third.getNickname());
        } catch (InvalidSelectionException invalidSelectionException) {
            invalidSelectionException.printStackTrace();
        }

        game.setTurnPlayer(player);
        worker.setPosition(game.getCell(1 , 1));
        player.getPlayerState().setSelectedWorker(worker);
        player.getPlayerState().setSelectedCell(game.getCell(1 , 2));
        game.setCurrentPlayerIndex(0);
    }

    @Test
    void stateInit() {
        phase = new ChooseBlockPhase(game , new BlockPredicate(0));
        game.initGame();
        phase.stateInit();
        assertEquals(null , player.getPlayerState().getSelectedConstructible());
    }

    @Test
    void run() throws IllegalFormatException, InvalidSelectionException {
        phase = new ChooseBlockPhase(game , new BlockPredicate(3));
        phase.stateInit();
        assertThrows(InvalidSelectionException.class , () -> phase.run("DOME"));
        phase.run("BLOCK");
        assertEquals(Constructible.BLOCK , player.getPlayerState().getSelectedConstructible());
    }
}