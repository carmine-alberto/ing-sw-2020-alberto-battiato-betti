package it.polimi.ingsw.model;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.actions.Action;

import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private String nickname;
    private String colour;
    private Boolean isWinner;
    private Game currentGame;
    private View playerView;

    private List<GameWorker> workers;

    public Player(String nickname, View playerView) {
        this.nickname = nickname;
        this.playerView = playerView;
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


    public View getPlayerView() {
        return playerView;
    }


    public List<Player> getOpponents(){
        return currentGame.getPlayers().stream()
                .filter(player -> !player.equals(this))
                .collect(Collectors.toList());

    }
}
