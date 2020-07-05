package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.view.serverView.VirtualBoardViewState;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.actionPredicate.CanBuildPredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanMovePredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsCellFreePredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsDeltaHeightLessThanPredicate;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.IsTurnPlayerPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GameSettings.*;

public class Game extends Observable<Event> {
    private final static Integer INITIAL_VALUE = -1;
    private final static Integer MIN_NUM_OF_NONCHALLENGERS = 1;

    private Integer numberOfPlayers;

    private Player turnPlayer;  //TODO ind turnPlayer to currentPlayerIndex to enforce synchronization
    private Integer currentPlayerIndex;
    private List<Player> players;
    private List<God> godPowers;

    private TurnPhase turnPhase;

    private FieldCell[][] field = new FieldCell[FIELD_SIZE][FIELD_SIZE];


    /**
     * Constructor for class Game, it creates the board and the list of available gods
     */
    public Game() {
        players = new ArrayList<>();

        numberOfPlayers = INITIAL_VALUE;

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                this.field[i][j] = new FieldCell(this, i, j);
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

    /**
     *
     * @param newNickname
     * @throws InvalidSelectionException
     */
    public void addPlayer(String newNickname, VirtualView playerView) throws InvalidSelectionException {
       for (Player tempPlayer : players)
           if (tempPlayer.getNickname().equals(newNickname))
               throw new InvalidSelectionException("Nickname already in use");

       Player newPlayer = new Player(newNickname, playerView);
       newPlayer.setCurrentGame(this);
       if (players.isEmpty())
           newPlayer.setChallenger(true);

       players.add(newPlayer);
       playerView.setOwner(newPlayer.getNickname());
    }

    /**
     * Sets the currentPlayerIndex, used to extract the turnPlayers from the players list
     * @param currentPlayerIndex as passed by view (incremented by one, to improve user experience)
     */
    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * called only at the starting of the game, it starts the first turn
     */
    public void initGame() {
        notifyObservers(new GameInformationEvent(players));
        notifyObservers(new GameStartedEvent());
        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);
        turnPhase.stateInit();
    }

    //TODO Make sure the transition is smooth - an error seemed to be present during one of our tests

    /**
     * removes the TurnPlayer from the game and ends the game if there's only one player left
     */
    public void removeTurnPlayer() {
        Player playerToRemove = turnPlayer;

        setNextTurnPlayer();
        players.remove(playerToRemove);

        if (players.size() == 1) {
            players.get(FIRST_ELEMENT_INDEX).setIsWinner(true);
            endGame();

            return;
        }
        playerToRemove.removeWorkersFromBoard();
        notifyObservers(new PlayerLostUpdate(playerToRemove.getNickname()));

        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);
        turnPhase.stateInit();
    }

    /**
     * Called after a player's victory, it stops the game and kills the server
     */
    public void endGame() {
        Player winner = players.stream()
                .filter(player -> player.getIsWinner())
                .collect(Collectors.toList())
                .get(FIRST_ELEMENT_INDEX);

        notifyObservers(new GameEndUpdate(winner.getNickname()));
        System.exit(0);
    }

    /**
     * called after the end of the turn, it changes the current turnPlayer to the next one
     */
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

    public String getTurnPlayerNickname() {
        if (turnPlayer == null)
            turnPlayer = players.get(currentPlayerIndex);
        return turnPlayer.getNickname();
    }

    /**
     * @return a list of all god's names
     */
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

        notifyObservers(new SelectedGodsUpdate(getGodPowers()));
    }

    /**
     * this method removes the given god from the GodsList
     * @param selectedGod
     */
    public void removeGodPowerFromAvailableGods(String selectedGod) {
        this.godPowers = this.godPowers
            .stream()
            .filter(godPower -> !godPower.getName().equals(selectedGod))
            .collect(Collectors.toList());

        notifyObservers(new SelectedGodsUpdate(getGodPowers()));
    }

    public void notifyObservers(Event e) {
        this.notify(e);
    }

    public void notifyTurnPlayer(Event event) {
        if (!observers.isEmpty())
            this.notify(observers
                    .stream()
                    .filter(observer -> observer.equals(turnPlayer.getPlayerView()))
                    .collect(Collectors.toList())
                    .get(0),
                    event);
    }

    public FieldCell[][] getField() {
        return this.field;
    }

    /**
     *runs the currentPhase
     * @param inputString usually indicates a cell's position, it may indicate actions or constructible items
     * @throws IllegalFormatException when parameter isn't recognised from the phase
     * @throws InvalidSelectionException when the parameter indicates a non permitted action
     */
    public void runPhase(String inputString) throws IllegalFormatException, InvalidSelectionException {
        this.turnPhase.run(inputString);
        turnPhase.stateEnd();

        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);

        turnPhase.stateInit();
    }

    /**
     * assigns the given god to the given player
     * @param selectedGod
     * @param choosingPlayer
     */
    public void assignSelectedGodPowerToPlayer(String selectedGod, String choosingPlayer) throws InvalidSelectionException {
        God godToBeAssigned = godPowers
            .stream()
            .filter(god -> selectedGod.equals(god.getName()))
            .findFirst()
            .orElseThrow(() -> new InvalidSelectionException("The selected God is not available!"));

        getPlayerByName(choosingPlayer).setSelectedGod(godToBeAssigned);
    }

    public void detachObservers() {
        observers.clear();
    }

    public Boolean isTurnPlayer(String nickname) {
        return turnPlayer.getNickname().equals(nickname);
    }

    public String getNthPlayer(Integer index) {
        return players.get(index).getNickname();
    }


    public void initGods() {
        Parser fileParser = new Parser();
        godPowers = fileParser.getGodsList();
        //godPowers = buildDefaultGods();
        notify(new AvailableGodsUpdate(getGodPowers()));
    }
    /**
     * For testing purposes - builds a set of default gods
     * @return a List of gods with default powers
     */
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

    public Boolean hasFreeSlots() {
        return numberOfPlayers == INITIAL_VALUE && players.size() <= MIN_NUM_OF_NONCHALLENGERS || players.size() < numberOfPlayers;
    }

    public Boolean haveAllPlayersConnected() {
        if (players.size() == numberOfPlayers) {
            notifyObservers(new SelectedGodsUpdate(getGodPowers()));
            return true;
        }

        return false;
    }

    public void registerChallengerSelection(Integer selectedStartingPlayerIndex, Integer selectedNumberOfPlayers, List<String> selectedGods) throws InvalidSelectionException {
        if (selectedNumberOfPlayers != selectedGods.size())
            throw new InvalidSelectionException("Number of players and god powers must match!");

        if (selectedStartingPlayerIndex > selectedNumberOfPlayers)
            throw new  InvalidSelectionException("You can't choose the third player as starting player in a 2-players game!");

        numberOfPlayers = selectedNumberOfPlayers;
        currentPlayerIndex = selectedStartingPlayerIndex - OFFSET;
        setGodPowers(selectedGods);
    }

    public Boolean isChallenger(String nickname) {
        try {
            return getPlayerByName(nickname).isChallenger();
        } catch (InvalidSelectionException e) {
            return false;
        }
    }


    private Player getPlayerByName(String nickname) throws InvalidSelectionException {
        return players
                .stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new InvalidSelectionException("The nickname provided does not belong to any player"));
    }

    public void handleColorAndWorkerSelection(String selectedColor, List<Integer> xCoordinates, List<Integer> yCoordinates) throws InvalidSelectionException {
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                if (field[i][j].getWorker() != null && field[i][j].getWorker().getOwner().getColour().toUpperCase().equals(selectedColor))
                    throw new InvalidSelectionException("One of your opponents already chose this color, pick another one!");


        if (xCoordinates.size() != NUM_OF_WORKERS || yCoordinates.size() != NUM_OF_WORKERS)
            throw new InvalidSelectionException("The selected positions are not " + NUM_OF_WORKERS);

        if(!field[xCoordinates.get(0) - 1][yCoordinates.get(0) - 1].isFree() || !field[xCoordinates.get(1) - 1][yCoordinates.get(1) - 1].isFree())
            throw new InvalidSelectionException("You can't place a worker in a cell that is already occupied");


        turnPlayer.setColour(selectedColor);

        List<GameWorker> turnPlayerWorkers = new ArrayList<>();
        for (Integer i = 0; i < NUM_OF_WORKERS; i++) {
            turnPlayerWorkers.add(new GameWorker(this, turnPlayer));
            try {
                turnPlayerWorkers.get(i).setPosition(field[xCoordinates.get(i) - 1][yCoordinates.get(i) - 1]);
            } catch (IndexOutOfBoundsException e) {
                throw new InvalidSelectionException("The selected cell does not exist on the board");
            }
        }
        turnPlayer.setWorkers(turnPlayerWorkers);
    }

    public Boolean isReadyToStart() {
        return players
            .stream()
            .filter(player -> player.getWorkers() != null)
            .count() == numberOfPlayers;
    }
}

