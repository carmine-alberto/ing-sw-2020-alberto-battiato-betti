package it.polimi.ingsw.model;

import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.IsTurnPlayerPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Player implements Serializable {
    private String nickname;
    private String colour;
    private Boolean isWinner;
    private transient Game currentGame;
    private transient VirtualView playerView;
    private List<GameWorker> workers;
    private String selectedGodPower; //TODO Refactor into proper type
    private God selectedGod;

    private transient PlayerState playerState;

    //Predicates
    private transient BiPredicate<Game, GameWorker> winConditions = new WinningMovePredicate().and(new IsTurnPlayerPredicate());

    public Player(String nickname){
        this.nickname = nickname;
        this.isWinner = false;
    }//TODO eliminare questo costruttore

    public Player(String nickname, VirtualView playerView) {
        this.nickname = nickname;
        this.playerView = playerView;
        this.playerState = new PlayerState(this);
        this.isWinner = false;
    }


    public BiPredicate<Game, GameWorker> getWinConditions() {
        return winConditions;
    }

    public void setWinConditions(BiPredicate<Game, GameWorker> winConditions) {
        this.winConditions = winConditions;
    }


    public List<GameWorker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<GameWorker> workers) {
        this.workers = workers;
    }


    public Boolean getIsWinner(){
        return this.isWinner;
    }

    public void setIsWinner(Boolean tmp){
        isWinner = tmp;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }


    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }


    public VirtualView getPlayerView() {
        return playerView;
    }


    public List<Player> getOpponents(){
        return currentGame.getPlayers().stream()
                .filter(player -> !player.equals(this))
                .collect(Collectors.toList());
    }

    public String getSelectedGodPower() {
        return selectedGodPower;
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public God getSelectedGod() {
        return selectedGod;
    }

    void setSelectedGod(God selectedGod) {
        this.selectedGod = selectedGod;
    }

    /**
     * Setting the worker "position" attribute to null is pointless;
     * it's never used if a worker is removed from the board
     */
    public void removeWorkersFromBoard() {
        workers.forEach(worker -> worker.getCell().setOccupyingWorker(null));
    }
}
