package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.function.BiPredicate;

public class EndPhase extends TurnPhase {

    public EndPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame);
    }

    /**
     * Runs the body of the EndPhase - placed here for compatibility with TurnPhases
     */
    @Override
    public void stateInit() {
        try {
            currentGame.runPhase(null);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        } catch (InvalidSelectionException e) {
            //Never thrown - inputString is null
        }

    }

    /**
     * Notifies the turnPlayer about the turn being over
     * @param arg Not used - placed here for compatibility
     * @throws IllegalFormatException Never thrown
     * @throws InvalidSelectionException Never thrown
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        turnPlayer = currentGame.getTurnPlayer();

        currentGame.notifyTurnPlayer(new PhaseUpdate("Your turn is over"));

        turnPlayer.getPlayerState().reset();

    }

    /**
     * Sets the next turnPlayer
     */
    @Override
    public void stateEnd() {
        super.stateEnd();
        currentGame.setNextTurnPlayer();
    }
}
