package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.function.BiPredicate;


public class ChooseWorkerPhase extends TurnPhase {

    public ChooseWorkerPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "chooseWorkerPredicate");
    }

    /**
     * here is sent a notification to the turn player
     */
    @Override
    public void stateInit() {
        currentGame.notifyTurnPlayer(new PhaseUpdate("Select the desired worker"));
    }

    /**
     * Here is setted the player's selected worker
     *
     * @param arg coordinates
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection is invalid
     */
    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        GameWorker selectedWorker = extractWorkerFromCoordinates(arg);
        turnPlayer.getPlayerState().setSelectedWorker(selectedWorker);
    }


    private GameWorker extractWorkerFromCoordinates(String coordinates) throws InvalidSelectionException {
        Integer x = Integer.parseInt(coordinates.substring(0, 1));
        Integer y = Integer.parseInt(coordinates.substring(2, 3));

        GameWorker extractedWorker = currentGame.getCell(x-1, y-1).getWorker();

        if (extractedWorker == null)
            throw new InvalidSelectionException("The selected cell contains no worker. Try again!");

        if (!extractedWorker.getOwner().equals(currentGame.getTurnPlayer()))
            throw new InvalidSelectionException("The selected worker is not yours. Try again!");

        return extractedWorker;
    }
}
