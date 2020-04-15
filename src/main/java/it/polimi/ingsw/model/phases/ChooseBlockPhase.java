package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseBlockPhase extends TurnPhase {
    List<Constructible> availableBlocks = new ArrayList<>(EnumSet.allOf(Constructible.class));

    public ChooseBlockPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    public void stateInit() {
        this.nextPhase = new ChooseWorkerPhase(currentGame);

    }

    @Override
    public void run(String arg) {
        //If we get here, the worker can certainly build something, no need to check the legality of the move

        Player turnPlayer = currentGame.getTurnPlayer();
        PlayerState turnPlayerState = turnPlayer.getPlayerState();

        availableBlocks = availableBlocks.stream()
                .filter(block -> turnPlayer.getBlockPredicate().test(turnPlayer, block))
                .collect(Collectors.toList());

        //If we get here, the worker can certainly build something, no need to check the legality of the move

        if (availableBlocks.size() == 1)
            turnPlayer.getPlayerState().setSelectedConstructible(availableBlocks.get(0));
        else {
            // TODO Send notification
            // TODO Wait for user response
        }

        turnPlayerState.getSelectedWorker().build(turnPlayerState.getSelectedCell(), turnPlayerState.getSelectedConstructible());
        turnPlayerState.getSelectedWorker().getOldBuildPositions().add(turnPlayerState.getSelectedCell());
        checkWinConditions();

    }

    @Override
    public void stateEnd() {
        super.stateEnd();
        currentGame.setNextTurnPlayer();
        //TODO empty whatever structure needs to be emptied (e.g. OldMovePositions)
    }
}
