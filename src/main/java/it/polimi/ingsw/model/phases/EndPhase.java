package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.function.BiPredicate;

public class EndPhase extends TurnPhase {
    private Player turnPlayer;

    public EndPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame);
    }

    /**
     * here is run the phase
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
     * here is sent a message to the player
     * @param arg
     * @throws IllegalFormatException
     * @throws InvalidSelectionException
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        turnPlayer = currentGame.getTurnPlayer();

        currentGame.notifyTurnPlayer(new PhaseUpdate("Your turn is over"));

        turnPlayer.getPlayerState().reset();

    }

    /**
     * here is set the next turn player
     */
    @Override
    public void stateEnd() {
        super.stateEnd();
        currentGame.setNextTurnPlayer();
        //TODO empty whatever structure needs to be emptied (e.g. OldMovePositions)
    }
}
