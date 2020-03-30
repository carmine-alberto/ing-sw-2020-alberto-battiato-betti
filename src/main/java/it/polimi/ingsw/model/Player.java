package it.polimi.ingsw.model;

import it.polimi.ingsw.model.predicates.movePredicates.IsCellFreePredicate;
import it.polimi.ingsw.model.predicates.movePredicates.IsDeltaHeightLessThanPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicate.HasMovedUpPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicate.IsTurnPlayerPredicate;
import it.polimi.ingsw.view.View;


import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
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
    private ActionEnum selectedAction;


    //Predicates
    private BiPredicate<FieldCell, GameWorker> movePredicate = new IsCellFreePredicate().and(new IsDeltaHeightLessThanPredicate());
    private BiPredicate<FieldCell, GameWorker> buildPredicate;
    private BiPredicate<FieldCell, GameWorker> blockPredicate;
    private BiPredicate<FieldCell, GameWorker> domePredicate;
    private Predicate<Player> actionPredicate;
    private BiPredicate<Game, GameWorker> winConditions = new HasMovedUpPredicate().and(new IsTurnPlayerPredicate()); //TODO Delegate winCondition assignment to a specific builder

    public BiPredicate<FieldCell, GameWorker> getMovePredicate() {
        return movePredicate;
    }

    public void setMovePredicate(BiPredicate<FieldCell, GameWorker> movePredicate) {
        this.movePredicate = movePredicate;
    }


    public BiPredicate<FieldCell, GameWorker> getBuildPredicate() {
        return buildPredicate;
    }

    public void setBuildPredicate(BiPredicate<FieldCell, GameWorker> buildPredicate) {
        this.buildPredicate = buildPredicate;
    }


    public BiPredicate<FieldCell, GameWorker> getBlockPredicate() {
        return blockPredicate;
    }

    public void setBlockPredicate(BiPredicate<FieldCell, GameWorker> blockPredicate) {
        this.blockPredicate = blockPredicate;
    }


    public BiPredicate<FieldCell, GameWorker> getDomePredicate() {
        return domePredicate;
    }

    public void setDomePredicate(BiPredicate<FieldCell, GameWorker> domePredicate) {
        this.domePredicate = domePredicate;
    }

    public Predicate<Player> getActionPredicate() {
        return actionPredicate;
    }

    public void setActionPredicate(Predicate<Player> actionPredicate) {
        this.actionPredicate = actionPredicate;
    }

    public BiPredicate<Game, GameWorker> getWinConditions() {
        return winConditions;
    }

    public void setWinConditions(BiPredicate<Game, GameWorker> winConditions) {
        this.winConditions = winConditions;
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


    public ActionEnum getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(ActionEnum selectedAction) {
        this.selectedAction = selectedAction;
    }
}
