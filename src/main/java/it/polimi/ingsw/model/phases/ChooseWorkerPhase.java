package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;

public class ChooseWorkerPhase extends TurnPhase {

    public ChooseWorkerPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        nextPhase = new ChooseWorkerPhase(currentGame);
        //Notify current player of the phase change
    }

    @Override
    protected void run() {
        //game.waitForEvent();

    }

    @Override
    protected void stateEnd() {

    }
}
