package it.polimi.ingsw.model;

import it.polimi.ingsw.model.phases.ChooseWorkerPhase;
import it.polimi.ingsw.model.phases.TurnPhase;
import java.util.ArrayList;
import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public Integer NUM_OF_PLAYERS;

    private Player turnPlayer;  //TODO ind turnPlayer to currentPlayerIndex to enforce synchronization
    private Integer currentPlayerIndex;
    private List<Player> players = new ArrayList<>();

    private TurnPhase turnPhase;

    private FieldCell[][] field = new FieldCell[FIELD_SIZE][FIELD_SIZE];

    public static Integer FIELD_SIZE = 5;

    public Game() {
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

    public void addPlayer(Player player) {
        try {
            for (Player tmp : players)
                if (tmp.getNickname().equals(player.getNickname()))
                    throw new AlreadyExistingNameException("Questo Nickname è stato già utilizzato, sceglierne un altro.");
            players.add(player);
        } catch (AlreadyExistingNameException e) {
            //TODO mostrare view con messaggio d'errore
        }
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
        turnPhase = new ChooseWorkerPhase(this);
        while (true) {
            turnPhase.runPhase();
            turnPhase = turnPhase.getNextPhase();
        }
    }

    public void setPhase(TurnPhase nextTurnPhase) {
        this.turnPhase = nextTurnPhase;
    }

    public void removeTurnPlayer() {
        players.remove(turnPlayer);
        currentPlayerIndex--; //Used to handle third to first player case
        if (players.size() == 1) {
            players.get(0).setIsWinner(true);
            endGame();
        }
    }

    public void endGame() {
        Player winner = players.stream()
                .filter(player -> player.getIsWinner())
                .collect(Collectors.toList())
                .get(0);
        //TODO Handle victory
    }

    public void setNextTurnPlayer() {
        this.turnPlayer = players.get(getNextPlayerIndex());
    }

    private Integer getNextPlayerIndex() {
        currentPlayerIndex++;
        return currentPlayerIndex % players.size();
    }


    public Player getTurnPlayer() {
        return turnPlayer;
    }
}