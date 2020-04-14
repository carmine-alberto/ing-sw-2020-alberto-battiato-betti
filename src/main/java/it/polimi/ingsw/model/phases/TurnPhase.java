package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;


public abstract class TurnPhase {
    protected TurnPhase nextPhase;
    protected Game currentGame;

    public TurnPhase(Game currentGame) {
        this.currentGame = currentGame;
    }


    protected abstract void stateInit();

    protected abstract void run();

    protected void stateEnd() {
        currentGame.getPlayers().forEach(player -> checkIsWinner(player));
        //TODO Invio notifica
    }

    public void runPhase() {
        stateInit();
        run();
        stateEnd();
    }


    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(player -> checkPlayerWinConditions(player));
    }


    private void checkPlayerWinConditions(Player player) {
        if (player.getWinConditions().test(currentGame, player.getPlayerState().getSelectedWorker()))
            player.setIsWinner(true);
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
