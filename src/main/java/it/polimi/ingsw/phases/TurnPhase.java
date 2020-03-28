package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.Game;


public abstract class TurnPhase {
    protected TurnPhase nextPhase;
    Game currentGame;

    public TurnPhase(Game currentGame) {
        this.currentGame = currentGame;
    }


    protected abstract void stateInit();

    protected abstract void run();

    protected void stateEnd() {
        //Invio notifica
    }

    public TurnPhase getNextPhase() {
        return this.nextPhase;
    }

    public void setNextPhase(TurnPhase nextTurnPhase) {
        this.nextPhase = nextTurnPhase;
    }

    public void runPhase() {
        stateInit();
        run();
        stateEnd();
    }
}
