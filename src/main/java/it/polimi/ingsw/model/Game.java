package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.GameEndUpdate;
import it.polimi.ingsw.controller.events.GameStartedEvent;
import it.polimi.ingsw.controller.events.PlayerLostUpdate;
import it.polimi.ingsw.cview.serverView.VirtualBoardView;
import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsCellFreePredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanBuildPredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanMovePredicate;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsDeltaHeightLessThanPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.IsTurnPlayerPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends Observable<Event> {
    public Integer NUM_OF_PLAYERS;

    private Player turnPlayer;  //TODO ind turnPlayer to currentPlayerIndex to enforce synchronization
    private Integer currentPlayerIndex;
    private List<Player> players;
    private List<God> godPowers;

    private TurnPhase turnPhase;

    private FieldCell[][] field = new FieldCell[FIELD_SIZE][FIELD_SIZE];

    public static Integer FIELD_SIZE = 5;

    public Game() {
        players = new ArrayList<>();

        /*Parser fileParser = new Parser();
        godPowers = fileParser.getGodsList();*/
        godPowers = buildDefaultGods();


        NUM_OF_PLAYERS = -1; //TODO Refactor into proper private variable + accessor

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                this.field[i][j] = new FieldCell(this, i, j);
    }

    private List<God> buildDefaultGods() {
        God.GodBuilder godBuilder = new God.GodBuilder();
        List<God> godsToReturn = new ArrayList<>();

        for (Integer i = 0; i < 3; i++)
            godsToReturn.add(
                godBuilder
                .name("Default" + i)
                .addPhase("ChooseWorkerPhase", (arg1, arg2) -> true)
                .addPhase("ChooseActionPhase", new CanMovePredicate().or(new CanBuildPredicate()))
                .saveRefNode()
                .addPhase("MovePhase", new IsCellFreePredicate().and(new IsDeltaHeightLessThanPredicate(2)))
                .addPhase("BuildPhase", new IsCellFreePredicate())
                .addPhase("ChooseBlockPhase", new BlockPredicate(3))
                .addPhase("EndPhase", (arg1, arg2) -> true)
                .restoreRefNode()
                .addPhase("BuildPhase", new IsCellFreePredicate())
                .addPhase("ChooseBlockPhase", new BlockPredicate(3))
                .addPhase("EndPhase", (arg1, arg2) -> true)
                .moveStrategy(new Move())
                .buildStrategy(new Build())
                .winConditionPredicate(new WinningMovePredicate().and(new IsTurnPlayerPredicate()))
                .getCompleteGod()
            );

        return godsToReturn;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setTurnPlayer(Player currentTurnPlayer) {
        this.turnPlayer = currentTurnPlayer;
    }

    public FieldCell getCell(Integer x, Integer y) {
        return field[x][y];
    }

    public void addPlayer(Player player) throws InvalidSelectionException {
       for (Player tmp : players)
                if (tmp.getNickname().equals(player.getNickname()))
                    throw new InvalidSelectionException("Nickname already in use");
            players.add(player);

    }

    /**
     * Sets the currentPlayerIndex, used to extract the turnPlayers from the players list
     *
     * @param currentPlayerIndex as passed by view (incremented by one, to improve user experience)
     */
    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void initGame() {
        notifyObservers(new GameStartedEvent());
        players.forEach(player -> player.getSelectedGod().assignWinConditionPredicate(player)); //TODO Should we do this somewhere else?
        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);
        turnPhase.stateInit();
    }

    public void removeTurnPlayer() {
        Player playerToRemove = turnPlayer;

        setNextTurnPlayer();
        players.remove(playerToRemove);

        if (players.size() == 1) {
            players.get(0).setIsWinner(true);
            endGame();

            return;
        }
        playerToRemove.removeWorkersFromBoard();
        notifyObservers(new PlayerLostUpdate(playerToRemove.getNickname()));

        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);
        turnPhase.stateInit();
    }

    public void endGame() {
        Player winner = players.stream()
                .filter(player -> player.getIsWinner())
                .collect(Collectors.toList())
                .get(0);

        notifyObservers(new GameEndUpdate(winner.getNickname()));
        //TODO What should we do here? Terminate the game threads and return?
    }

    public void setNextTurnPlayer() {
        this.turnPlayer = players.get(getNextPlayerIndex());
    }

    private Integer getNextPlayerIndex() {
        currentPlayerIndex++;
        return currentPlayerIndex % players.size();
    }


    public Player getTurnPlayer() {
        if (turnPlayer == null)
            turnPlayer = players.get(currentPlayerIndex);
        return turnPlayer;
    }


    public List<String> getGodPowers() {
        return godPowers
                .stream()
                .map(godPower -> godPower.getName())
                .collect(Collectors.toList());

    }

    public void setGodPowers(List<String> godPowers) {
        this.godPowers = this.godPowers
        .stream()
        .filter(godPower -> godPowers.contains(godPower.getName()))
        .collect(Collectors.toList());
    }

    public void removeGodPowerFromAvailableGods(String selectedGod) {
        this.godPowers = this.godPowers
            .stream()
            .filter(godPower -> !godPower.getName().equals(selectedGod))
            .collect(Collectors.toList());
    }

    public void notifyObservers(Event e) {
        this.notify(e);
    }

    public void notifyTurnPlayer(Event event) {
        this.notify(observers
                .stream()
                .filter(observer -> ((VirtualBoardView)observer).getVirtualView().equals(turnPlayer.getPlayerView()))
                .collect(Collectors.toList())
                .get(0),
                event);
    }

    FieldCell[][] getField() {
        return this.field;
    }

    public void runPhase(String inputString) throws IllegalFormatException, InvalidSelectionException {
        this.turnPhase.run(inputString);
        turnPhase.stateEnd();

        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);

        turnPhase.stateInit();
    }

    public void assignSelectedGodPowerToPlayer(String selectedGod, Player choosingPlayer) {
        God godToBeAssigned = godPowers
            .stream()
            .filter(god -> selectedGod.equals(god.getName()))
            .findFirst()
            .get();

        choosingPlayer.setSelectedGod(godToBeAssigned);
    }
}
