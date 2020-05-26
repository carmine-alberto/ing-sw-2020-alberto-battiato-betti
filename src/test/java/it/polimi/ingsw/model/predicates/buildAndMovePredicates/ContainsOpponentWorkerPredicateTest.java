package it.polimi.ingsw.model.predicates.buildAndMovePredicates;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainsOpponentWorkerPredicateTest {
    Game game;
    List<Player> players;

    @BeforeEach
    void setUp() throws InvalidSelectionException {
        setupGame();
        game.setTurnPlayer(players.get(0));
        game.initGame();
    }

    @Test
    void shift() throws IllegalFormatException, InvalidSelectionException {
        assertEquals(game.getCell(0 , 0).getWorker() , players.get(0).getWorkers().get(0));
        game.runPhase("1 1");
        game.runPhase("2 2");
        assertEquals(game.getCell(0 , 0).getWorker() , players.get(1).getWorkers().get(0));
    }

    void setupGame() throws InvalidSelectionException {
        game = new Game();

        List<String> players = new ArrayList<>();
        List<String> godPowers = new ArrayList<>();

        players.add("Jonny");
        godPowers.add("Apollo");
        players.add("Il Peloso");
        godPowers.add("Artemis");

        setPlayers(players , godPowers);

        /*
         *    0   1   2   3   4
         * 0  O       X
         * 1      X   O
         * 2
         * 3
         * 4
         *
         * */
        List<FieldCell> positions = new ArrayList<>();
        positions.add(game.getCell(0,0));
        positions.add(game.getCell(1,2));
        positions.add(game.getCell(1,1));
        positions.add(game.getCell(0,2));

        setWorkers(positions);
    }

    void setPlayers(List<String> names , List<String> powers) throws InvalidSelectionException {
        players = new ArrayList<>();

        for (int i = 0;  i < names.size(); i++) {
            players.add(new Player(names.get(i) , null));
            game.addPlayer(players.get(i));
            game.assignSelectedGodPowerToPlayer(powers.get(i) , players.get(i));
        }
    }

    void setWorkers(List<FieldCell> positions){
        Integer numOfPlayers = positions.size() / 2;

        for (Integer i = 0 , j = 0; i < numOfPlayers; i++ , j++) {
            List<GameWorker> gw = new ArrayList<>();
            gw.add(new GameWorker(game, players.get(i)));
            gw.get(0).setPosition(positions.get(j));
            j++;
            gw.add(new GameWorker(game, players.get(i)));
            gw.get(1).setPosition(positions.get(j));
            players.get(i).setWorkers(gw);
        }
    }
}