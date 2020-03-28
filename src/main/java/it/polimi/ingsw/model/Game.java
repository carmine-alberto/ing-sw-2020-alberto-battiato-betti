package it.polimi.ingsw.model;

import it.polimi.ingsw.phases.ChooseWorkerPhase;
import it.polimi.ingsw.phases.TurnPhase;
import java.util.ArrayList;
import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private Player turnPlayer;
    Integer currentPlayerIndex;
    List<Player> players = new ArrayList<>();

    private TurnPhase turnPhase;

    private FieldCell[][] field = new FieldCell[5][5];

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
                else players.add(player);
        } catch (AlreadyExistingNameException e) {
            //TODO mostrare wiew con messaggio d'errore
        }
    }

    /**
     * Sets the currentPlayerIndex, used to extract the turnPlayers from the players list
     *
     * @param currentPlayerIndex as passed by view (incremented by one, to improve user experience)
     */
    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex - 1;
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
        return ++currentPlayerIndex % players.size();
    }


    public Player getTurnPlayer() {
        return turnPlayer;
    }
}