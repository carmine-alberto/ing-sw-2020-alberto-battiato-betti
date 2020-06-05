package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.phases.*;
import it.polimi.ingsw.model.predicates.actionPredicate.CanBuildPredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsCellFreePredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class GodTest {
    God god;
    God.GodBuilder godBuilder;
    Game game;
    List<Player> players = new ArrayList<>();

    @BeforeEach
    void setUp() throws InvalidSelectionException {
        godBuilder = new God.GodBuilder();
        setupGame();
        game.setTurnPlayer(players.get(0));
        game.initGame();
    }

    void setupGame() throws InvalidSelectionException {
        game = new Game();

        List<String> players = new ArrayList<>();
        List<String> godPowers = new ArrayList<>();

        players.add("Jonny");
        godPowers.add("Minotaur");
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

    @Test
    void nameWithoutPhases() {
        godBuilder.name("Caio");
        god = godBuilder.getCompleteGod();
        assertEquals(god.getName(), "Caio");
    }

    @Test
    void nameWithPhases(){
        godBuilder.name("Caio");
        godBuilder.addPhase("ChooseActionPhase", new CanBuildPredicate());
        god = godBuilder.getCompleteGod();
        assertEquals(god.getName(), "Caio");
    }

    @Test
    void addPhaseWithoutPhasePredicate(){
        godBuilder.addPhase("BuildPhase", null);
        god = godBuilder.getCompleteGod();
        BuildPhase buildPhase = new BuildPhase(game, new IsCellFreePredicate());
        BuildPhase realBuildPhase = (BuildPhase) god.getNextPhase(game);
        assertEquals(buildPhase.getClass(), realBuildPhase.getClass());
    }

    @Test
    void addOuterHashMap(){
        BiPredicate<FieldCell, GameWorker> isCellFree = new IsCellFreePredicate();
        godBuilder.addOuterHashMap("BuildPhase", isCellFree);
        god = godBuilder.getCompleteGod();
        assertEquals(isCellFree, god.getOuterPredicate("BuildPhase"));
    }

    @Test
    void saveRefNode(){
        IsCellFreePredicate isCellFreePredicate = new IsCellFreePredicate();
        godBuilder.addPhase("BuildPhase", isCellFreePredicate);
        //godBuilder.getCompleteGod().getNextPhase(game);
        assertEquals(new BuildPhase(game, isCellFreePredicate).getClass(), godBuilder.saveRefNode().getCompleteGod().getNextPhase(game).getClass());
    }

    @Test
    void buildStrategy(){
        Build build = new Build();
        godBuilder.buildStrategy(build);
        assertEquals(godBuilder.getCompleteGod().getBuildStrategy().toString(), build.toString());
    }

    @Test
    void getNextPhase(){
        assertThrows(Exception.class, () ->{
                        god.getNextPhase(game);
                        assertNull(god.getNextPhase(game));
                        });
    }

    @Test
    void setNextPhase(){
        god = godBuilder.getCompleteGod();
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), ChooseActionPhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), MovePhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), BuildPhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), ChooseBlockPhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), EndPhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), ChooseWorkerPhase.class);
        god.setNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), ChooseActionPhase.class);
    }

    @Test
    void setNextPhaseVar(){
        godBuilder.addPhase("MovePhase", new IsCellFreePredicate());
        godBuilder.addPhase("MovePhase", new IsCellFreePredicate());
        godBuilder.addPhase("BuildPhase", new IsCellFreePredicate());
        god = godBuilder.getCompleteGod();
        god.getNextPhase(game);
        assertEquals(god.getNextPhase(game).getClass(), MovePhase.class);
    }

    @Test
    void getWinConditions(){
        WinningMovePredicate winningMovePredicate = new WinningMovePredicate();
        IsCellFreePredicate isCellFreePredicate = new IsCellFreePredicate();

        godBuilder.winConditionPredicate(winningMovePredicate);
        godBuilder.addOuterHashMap("BuildPhase", isCellFreePredicate);
        god = godBuilder.getCompleteGod();
        assertEquals(winningMovePredicate, god.getWinCondition());
        assertEquals(isCellFreePredicate, god.getOuterPredicate("BuildPhase"));
    }
}