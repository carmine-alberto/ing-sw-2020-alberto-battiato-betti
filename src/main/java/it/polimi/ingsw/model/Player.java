package it.polimi.ingsw.model;

import it.polimi.ingsw.actions.Move;
import it.polimi.ingsw.model.predicates.MovePredicate;
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

    //Attributes used to store turnState
    private GameWorker selectedWorker;
    private FieldCell selectedCell;
    private Constructible selectedConstructible;
    private Action selectedAction;


    //Predicates
    MovePredicate movePredicate = new MovePredicate();

    public MovePredicate getMovePredicate() {
        return movePredicate;
    }

    public void setMovePredicate(MovePredicate movePredicate) {
        this.movePredicate = movePredicate;
    }

    public Player(String nickname){
        this.nickname = nickname;
    }//TODO eliminare questo costruttore

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


    public GameWorker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(GameWorker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }


    public FieldCell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(FieldCell selectedCell) {
        this.selectedCell = selectedCell;
    }


    public Constructible getSelectedConstructible() {
        return selectedConstructible;
    }

    public void setSelectedConstructible(Constructible selectedConstructible) {
        this.selectedConstructible = selectedConstructible;
    }


    public Action getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(Action selectedAction) {
        this.selectedAction = selectedAction;
    }
}
