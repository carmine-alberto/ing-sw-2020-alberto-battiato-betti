package it.polimi.ingsw.model.predicates.winConditionsPredicates;

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

class CompleteTowersPredicateTest {
    Game game;
    List<Player> players;

    @BeforeEach
    void setUp() throws InvalidSelectionException {
        setupGame();
        game.setTurnPlayer(players.get(0));
        game.initGame();
    }

    @Test
    void test() throws IllegalFormatException, InvalidSelectionException {
        for (Integer i = 0; i < 3 ; i++) {
            game.getCell(3, 3).incrementHeight();
            game.getCell(3,4).incrementHeight();
            game.getCell(4,3).incrementHeight();
            game.getCell(4 ,4).incrementHeight();
            game.getCell(2 , 3).incrementHeight();
        }
        game.getCell(3, 3).placeDome();
        game.getCell(3,4).placeDome();
        game.getCell(4,3).placeDome();
        game.getCell(4 ,4).placeDome();
        game.getCell(2 , 3).placeDome();
        assertTrue(new CompleteTowersPredicate(5).test(game, null)); //if this statement is reached, the game thread has not been closed, i.e. the winCondition doesn't work because the game is not over
    }

    void setupGame() throws InvalidSelectionException {
        game = new Game();

        List<String> players = new ArrayList<>();
        List<String> godPowers = new ArrayList<>();

        players.add("Jonny");
        godPowers.add("Chronus");
        players.add("Il Peloso");
        godPowers.add("Apollo");

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
            game.getPlayers().add(players.get(i));
            players.get(i).setCurrentGame(game);
            game.initGods();
            game.assignSelectedGodPowerToPlayer(powers.get(i) , players.get(i).getNickname());
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