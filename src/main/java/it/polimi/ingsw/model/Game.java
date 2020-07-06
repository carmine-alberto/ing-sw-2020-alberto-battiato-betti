package it.polimi.ingsw.model;

import it.polimi.ingsw.utility.Observable;
import it.polimi.ingsw.controller.events.*;
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

import static it.polimi.ingsw.utility.GameSettings.*;

public class Game extends Observable<Event> {
    private final static Integer INITIAL_VALUE = -1;
    private final static Integer MIN_NUM_OF_NONCHALLENGERS = 1;

    private Integer numberOfPlayers;

    private Player turnPlayer;
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
     * Adds a new player to the game
     * @param newNickname The nickname of the player to be added
     * @throws InvalidSelectionException if the player's name is already in use
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
    }

    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Sends game information to players and starts the first turn.
     * Called only at the starting of the game
     */
    public void initGame() {
        notifyObservers(new GameInformationEvent(players));
        notifyObservers(new GameStartedEvent());
        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);
        turnPhase.stateInit();
    }

    //TODO Make sure the transition is smooth - an error seemed to be present during one of our tests

    /**
     * Removes the turnPlayer from the game and ends the game if there's only one player left
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
     * Stops the game and kills the server.
     * Called after a player's victory
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
     * Changes the current turnPlayer to the next one.
     * Called after the end of the turn
     */
    public void setNextTurnPlayer() {
        this.turnPlayer = players.get(getNextPlayerIndex());
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
     * @return a list of each god's name. If no godPowers are set, returns null instead
     */
    public List<String> getGodPowers() {
        if (godPowers != null)
            return godPowers
                    .stream()
                    .map(godPower -> godPower.getName())
                    .collect(Collectors.toList());
        return null;

    }

    /**
     * Sets the godPowers that will be available to players
     * @param godPowers A list of strings representing god names
     */
    public void setGodPowers(List<String> godPowers) {
        this.godPowers = this.godPowers
        .stream()
        .filter(godPower -> godPowers.contains(godPower.getName()))
        .collect(Collectors.toList());

        notifyObservers(new SelectedGodsUpdate(getGodPowers()));
    }

    /**
     * This method removes the given god from the gods available to players
     * @param selectedGod The name of the god to remove
     */
    public void removeGodPowerFromAvailableGods(String selectedGod) {
        this.godPowers = this.godPowers
            .stream()
            .filter(godPower -> !godPower.getName().equals(selectedGod))
            .collect(Collectors.toList());

        notifyObservers(new SelectedGodsUpdate(getGodPowers()));
    }

    /**
     * Sends a notification to all of the observers about a specific event
     * @param e The event the observers will receive
     */
    public void notifyObservers(Event e) {
        this.notify(e);
    }

    /**
     * Sends a notification to the turnPlayer about a specific event
     * @param event The even the turnPlayer will receive
     */
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
     * Runs the currentPhase of the game
     * @param inputString usually indicates a cell's position, it may indicate actions or constructible items
     * @throws IllegalFormatException when the inpuString can't be parsed
     * @throws InvalidSelectionException when the inputString is parsed to a non-permitted action
     */
    public void runPhase(String inputString) throws IllegalFormatException, InvalidSelectionException {
        this.turnPhase.run(inputString);
        turnPhase.stateEnd();

        turnPhase = turnPlayer.getSelectedGod().getNextPhase(this);

        turnPhase.stateInit();
    }

    /**
     * Assigns the god passed as a String representing its name to the player passed as a String representing their nickname
     * @param selectedGod A String representing the god's name
     * @param choosingPlayer A String representing the player's nickname
     */
    public void assignSelectedGodPowerToPlayer(String selectedGod, String choosingPlayer) throws InvalidSelectionException {
        God godToBeAssigned = godPowers
            .stream()
            .filter(god -> selectedGod.equals(god.getName()))
            .findFirst()
            .orElseThrow(() -> new InvalidSelectionException("The selected God is not available!"));

        getPlayerByName(choosingPlayer).setSelectedGod(godToBeAssigned);
    }

    /**
     * Checks whether the passed nickname belongs to the turnPlayer
     * @param nickname A String representing a nickname
     * @return true if the nickname belongs to the turnPlayer
     */
    public Boolean isTurnPlayer(String nickname) {
        return turnPlayer.getNickname().equals(nickname);
    }

    /**
     * Returns the nickname of the nth player
     * @param index An Integer representing the position of a player (order criterion: login time)
     * @return The nickname of the nth player
     */
    public String getNthPlayer(Integer index) {
        return players.get(index).getNickname();
    }

    /**
     * Populates the game with all the existing Santorini gods and sends a List of their names as strings to the players
     */
    public void initGods() {
        Parser fileParser = new Parser();
        godPowers = fileParser.getGodsList();
        //godPowers = buildDefaultGods(); - LEFT FOR TESTING PURPOSES
        notify(new AvailableGodsUpdate(getGodPowers()));
    }

    /**
     * Checks whether the current game can accept more players
     * @return true if more players can join the game
     */
    public Boolean hasFreeSlots() {
        return numberOfPlayers == INITIAL_VALUE && players.size() <= MIN_NUM_OF_NONCHALLENGERS || players.size() < numberOfPlayers;
    }

    /**
     * Checks whether the current game has reached the expected number of players, as decided by the challenger
     * @return true if the expected number of players is reached
     */
    public Boolean haveAllPlayersConnected() {
        if (players.size() == numberOfPlayers) {
            notifyObservers(new SelectedGodsUpdate(getGodPowers()));
            return true;
        }

        return false;
    }

    /**
     * Sets the starting player, the number of players and the gods that will be available in the game being created
     * @param selectedStartingPlayerIndex The starting player index (challenger: 1, second player: 2, etc...)
     * @param selectedNumberOfPlayers The number of players to reach before the game can start
     * @param selectedGods The gods that will be available for the players to pick
     * @throws InvalidSelectionException when the passed parameter are in conflict with each other
     */
    public void registerChallengerSelection(Integer selectedStartingPlayerIndex, Integer selectedNumberOfPlayers, List<String> selectedGods) throws InvalidSelectionException {
        if (selectedNumberOfPlayers != selectedGods.size())
            throw new InvalidSelectionException("Number of players and god powers must match!");

        if (selectedStartingPlayerIndex > selectedNumberOfPlayers)
            throw new  InvalidSelectionException("You can't choose the third player as starting player in a 2-players game!");

        numberOfPlayers = selectedNumberOfPlayers;
        currentPlayerIndex = selectedStartingPlayerIndex - OFFSET;
        setGodPowers(selectedGods);
    }

    /**
     * Sets the turnPlayer's color and creates the two gameworkers of theirs according to the specified coordinates
     * @param selectedColor A String representing the chosen color
     * @param xCoordinates A List containing the x coordinate of the two workers expressed as Integers
     * @param yCoordinates A List containing the y coordinate of the two workers expressed as Integers
     * @throws InvalidSelectionException when the passed parameter are in conflict with each other
     */
    public void handleColorAndWorkerSelection(String selectedColor, List<Integer> xCoordinates, List<Integer> yCoordinates) throws InvalidSelectionException {
        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                if (field[i][j].getWorker() != null && field[i][j].getWorker().getOwner().getColour().toUpperCase().equals(selectedColor))
                    throw new InvalidSelectionException("One of your opponents already chose this color, pick another one!");


        if (xCoordinates.size() != NUM_OF_WORKERS || yCoordinates.size() != NUM_OF_WORKERS)
            throw new InvalidSelectionException("The selected positions are not " + NUM_OF_WORKERS);

        if(!field[xCoordinates.get(FIRST_ELEMENT_INDEX) - OFFSET][yCoordinates.get(FIRST_ELEMENT_INDEX) - OFFSET].isFree() || !field[xCoordinates.get(SECOND_ELEMENT_INDEX) - OFFSET][yCoordinates.get(SECOND_ELEMENT_INDEX) - OFFSET].isFree())
            throw new InvalidSelectionException("You can't place a worker in a cell that is already occupied");


        turnPlayer.setColour(selectedColor);

        List<GameWorker> turnPlayerWorkers = new ArrayList<>();
        for (Integer i = 0; i < NUM_OF_WORKERS; i++) {
            turnPlayerWorkers.add(new GameWorker(this, turnPlayer));
            try {
                turnPlayerWorkers.get(i).setPosition(field[xCoordinates.get(i) - OFFSET][yCoordinates.get(i) - OFFSET]);
            } catch (IndexOutOfBoundsException e) {
                throw new InvalidSelectionException("The selected cell does not exist on the board");
            }
        }
        turnPlayer.setWorkers(turnPlayerWorkers);
    }

    /**
     * Checks whether the passed nickname belongs to the challenger
     * @param nickname A String representing the challenger's nickname
     * @return true if the nickname belongs to the challenger
     */
    public Boolean isChallenger(String nickname) {
        try {
            return getPlayerByName(nickname).isChallenger();
        } catch (InvalidSelectionException e) {
            return false;
        }
    }

    /**
     * Checks whether the current game is ready to start
     * @return true if all the players have chosen their color and gameWorkers
     */
    public Boolean isReadyToStart() {
        return players
            .stream()
            .filter(player -> player.getWorkers() != null)
            .count() == numberOfPlayers;
    }


    private Player getPlayerByName(String nickname) throws InvalidSelectionException {
        return players
                .stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new InvalidSelectionException("The nickname provided does not belong to any player"));
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

    private Integer getNextPlayerIndex() {
        currentPlayerIndex++;
        return currentPlayerIndex % players.size();
    }
}

