package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.Game;

public class ChooseBlockPhase extends TurnPhase {
    public ChooseBlockPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        this.nextPhase = new ChooseWorkerPhase(currentGame);

    }

    @Override
    protected void run() {
        //Scorri predicati per determinare i blocchi disponibili
        //Invia notifica al giocatore di turno
        //Attendi la risposta
        //Chiama build(destinazione, oggetto)

    }

    @Override
    protected void stateEnd() {
        super.stateEnd();
        currentGame.setNextTurnPlayer();
    }
}
