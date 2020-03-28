package it.polimi.ingsw.model;

import it.polimi.ingsw.phases.ChooseWorkerPhase;
import it.polimi.ingsw.phases.TurnPhase;

import java.util.ArrayList;
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

    public FieldCell getCell(Integer x, Integer y) {
        return field[x][y];
    }

    public GameWorker getWorkerFromPos(Integer x, Integer y){
        GameWorker worker = null;

        return worker;
    }

    public Boolean isCellAvailable(Integer x, Integer y){
        FieldCell cell = null; // TODO da modificare

        return cell.isFree();
    }

    /**
     * Sets the currentPlayerIndex, used to extract the turnPlayers from the players list
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
        currentPlayerIndex--;
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
