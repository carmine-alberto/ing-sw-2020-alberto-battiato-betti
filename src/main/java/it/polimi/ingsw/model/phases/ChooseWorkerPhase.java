package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;


public class ChooseWorkerPhase extends TurnPhase {

    public ChooseWorkerPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    public void stateInit() {
        nextPhase = new ChooseActionPhase(currentGame);
        currentGame.notifyTurnPlayer(new PhaseUpdate("Select the desired worker"));
    }

    @Override
    public void run(String arg) throws IllegalFormatException, InvalidSelectionException {
        parseCoordinatesArg(arg);

        GameWorker selectedWorker = extractWorkerFromCoordinates(arg);
        currentGame.getTurnPlayer().getPlayerState().setSelectedWorker(selectedWorker);
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
