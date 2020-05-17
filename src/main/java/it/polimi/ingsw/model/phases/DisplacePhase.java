package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.function.BiPredicate;

public class DisplacePhase extends TurnPhase {
    public DisplacePhase(Game currentGame) {
        super(currentGame);
    }

    public DisplacePhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "displacePredicate");
    }

    @Override
    public void stateInit() {

    }

    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        //faccio controlli, invio update con le caselle, ricevo la risposta
    }
}
