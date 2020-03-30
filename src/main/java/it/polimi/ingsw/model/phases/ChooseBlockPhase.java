package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChooseBlockPhase extends TurnPhase {
    List<Constructible> availableBlocks = new ArrayList<>();

    public ChooseBlockPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        this.nextPhase = new ChooseWorkerPhase(currentGame);

    }

    @Override
    protected void run() {
        Player turnPlayer = currentGame.getTurnPlayer();

        if (turnPlayer.getBlockPredicate().test(turnPlayer.getSelectedCell(), turnPlayer.getSelectedWorker())) //TODO Do we really need a BiPredicate here? FieldCell should be enough
            availableBlocks.add(Constructible.BLOCK);

        if (turnPlayer.getDomePredicate().test(turnPlayer.getSelectedCell(), turnPlayer.getSelectedWorker()))   //TODO same check as ChooseActionPhase
            availableBlocks.add(Constructible.DOME);

        //If we get here, the worker can certainly build something, no need to check the legality of the move

        if (availableBlocks.size() == 1)
            turnPlayer.setSelectedConstructible(availableBlocks.get(0));
        else {
            // TODO Send notification
            // TODO Wait for user response
        }

        turnPlayer.getSelectedWorker().build(turnPlayer.getSelectedCell(), turnPlayer.getSelectedConstructible());
        turnPlayer.getSelectedWorker().getOldBuildPositions().add(turnPlayer.getSelectedCell());
        checkWinConditions();

    }

    @Override
    protected void stateEnd() {
        super.stateEnd();
        currentGame.setNextTurnPlayer();
    }
}
