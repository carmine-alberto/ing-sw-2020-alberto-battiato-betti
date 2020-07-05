package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.function.BiPredicate;

import static it.polimi.ingsw.GameSettings.*;


public class ChooseWorkerPhase extends TurnPhase {

    public ChooseWorkerPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "chooseWorkerPredicate");
    }

    /**
     * Prompts the turnPlayer to select a worker
     */
    @Override
    public void stateInit() {
        currentGame.notifyTurnPlayer(new PhaseUpdate("Select the desired worker"));
    }

    /**
     * Sets the player's selected worker
     *
     * @param arg The worker selected by the turnPLayer
     * @throws IllegalFormatException If the string format does not fit the required one
     * @throws InvalidSelectionException If the selected cell doesn't contain any valid worker
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        GameWorker selectedWorker = extractWorkerFromCoordinates(arg);
        turnPlayer.getPlayerState().setSelectedWorker(selectedWorker);
    }


    private GameWorker extractWorkerFromCoordinates(String coordinates) throws InvalidSelectionException {
        Integer x = Integer.parseInt(coordinates.substring(FIRST_ELEMENT_INDEX, X_STARTING_POSITION));
        Integer y = Integer.parseInt(coordinates.substring(Y_STARTING_POSITION, EXPECTED_LENGTH));

        GameWorker extractedWorker = currentGame.getCell(x - OFFSET, y - OFFSET).getWorker();

        if (extractedWorker == null)
            throw new InvalidSelectionException("The selected cell contains no worker. Try again!");

        if (!extractedWorker.getOwner().equals(currentGame.getTurnPlayer()))
            throw new InvalidSelectionException("The selected worker is not yours. Try again!");

        return extractedWorker;
    }
}
