package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


public abstract class TurnPhase {
    protected TurnPhase nextPhase;
    protected Game currentGame;
    protected BiPredicate phasePredicate;
    protected BiPredicate outerPredicate;

    public TurnPhase(Game currentGame){
        this.currentGame = currentGame;
    }

    public TurnPhase(Game currentGame, BiPredicate phasePredicate) {
        this.currentGame = currentGame;
        this.phasePredicate = phasePredicate;
    }

    public abstract void stateInit();

    public abstract void run(String arg) throws IllegalFormatException, InvalidSelectionException;

    public void stateEnd() {
        currentGame.getPlayers().forEach(player -> checkIsWinner(player));
        currentGame.getTurnPlayer().getSelectedGod().setNextPhase(currentGame);
        //TODO Invio notifica

    }

    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(player -> checkPlayerWinConditions(player));
    }

    protected void parseCoordinatesArg(String arg) throws IllegalFormatException {
        if (arg.length() == 3) {
            try {
                Integer x = Integer.parseInt(arg.substring(0, 1));
                Integer y = Integer.parseInt(arg.substring(2, 3));

                if (x < 6 && x > 0 && y < 6 && y > 0)
                    return;
            } catch (NumberFormatException e) {
                throw new IllegalFormatException("Your selection's format was not recognized; try again");
            }
        }
        throw new IllegalFormatException("Your selection's format was not recognized; try again");
    }

    protected void removeTurnPlayerFromGame() {
        currentGame.removeTurnPlayer();
    }

    protected List<String> stringify(List<?> availableEnumItems) {
        return availableEnumItems
                .stream()
                .map(action -> action.toString())
                .collect(Collectors.toList());
    }

    protected FieldCell extractCellFromCoordinates(String coordinates) throws InvalidSelectionException {
        Integer x = Integer.parseInt(coordinates.substring(0, 1));
        Integer y = Integer.parseInt(coordinates.substring(2, 3));

        return currentGame.getCell(x-1, y-1);

    }

    private void checkPlayerWinConditions(Player player) {
        BiPredicate <Game, GameWorker> winConditions = player.getWinConditions();

        player.getOpponents().forEach(opponent -> {
            if(opponent.getSelectedGod().getOuterPredicate("winCondition") != null) {
                if (winConditions.and(opponent.getSelectedGod().getOuterPredicate("winCondition")).test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
            } else
                if (winConditions.test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
        });
    }

    private void checkIsWinner(Player player) {
        if (player.getIsWinner()) {
            currentGame.endGame();
        }
    }

    public TurnPhase getNextPhase() {
        return this.nextPhase;
    }

    public void setNextPhase(TurnPhase nextTurnPhase) {
        this.nextPhase = nextTurnPhase;
    }

}
