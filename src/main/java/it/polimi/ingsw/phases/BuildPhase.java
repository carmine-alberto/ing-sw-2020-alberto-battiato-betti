package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.Game;

public class BuildPhase extends TurnPhase {

    public BuildPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        this.nextPhase = new ChooseBlockPhase(currentGame);

    }

    @Override
    protected void run() {
        //Scorro i predicati e vedo quali oggetti si possono costruire

    }

    @Override
    protected void stateEnd() {

    }
}
