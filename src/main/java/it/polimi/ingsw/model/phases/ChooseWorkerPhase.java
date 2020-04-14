package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;

public class ChooseWorkerPhase extends TurnPhase {

    public ChooseWorkerPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        nextPhase = new ChooseActionPhase(currentGame);
        currentGame.notifyTurnPlayer(new PhaseUpdate("Select the desired worker"));
    }

    @Override
    protected void run() {
        synchronized (currentGame.getTurnPlayer()) {
            while (currentGame.getTurnPlayer().getNeedToWaitLockObject()) {
                try {
                    System.out.println("inside new thread");
                    currentGame.getTurnPlayer().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentGame.getTurnPlayer().setNeedToWait(true);
            if (currentGame.getTurnPlayer().getPlayerState().getSelectedWorker() != null)
                System.out.println(currentGame.getTurnPlayer().getPlayerState().getSelectedWorker());
            else
                System.out.println("Found null");
        }
    }

    @Override
    protected void stateEnd() {

    }
}
