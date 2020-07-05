package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndPhaseTest {
    Game game;
    Player player , second , third;
    GameWorker worker;
    TurnPhase phase;

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
        try {
            game.assignSelectedGodPowerToPlayer("Apollo" , player.getNickname());
            game.assignSelectedGodPowerToPlayer("Artemis" , second.getNickname());
            game.assignSelectedGodPowerToPlayer("Athena" , third.getNickname());
        } catch (InvalidSelectionException e) {
            e.printStackTrace();
        }

        worker = new GameWorker(game , player);

        game.setTurnPlayer(player);
        game.setCurrentPlayerIndex(0);

        player.getPlayerState().setSelectedWorker(worker);
        player.getPlayerState().setSelectedCell(game.getCell(0 , 0));

        player.getSelectedGod().setNextPhase(game);
        player.getSelectedGod().setNextPhase(game);
        player.getSelectedGod().setNextPhase(game);
        player.getSelectedGod().setNextPhase(game);
        player.getSelectedGod().setNextPhase(game);
        phase = new EndPhase(game , null);
    }

    @Test
    void stateInit() {
        game.initGame();
        assertEquals(second , game.getTurnPlayer());
        assertEquals(null , player.getPlayerState().getSelectedWorker());
    }
}