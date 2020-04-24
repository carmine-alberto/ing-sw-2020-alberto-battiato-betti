package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableChoicesUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ChooseBlockPhase extends TurnPhase {
    Player turnPlayer;
    private BiPredicate<Player, Constructible> blockPredicate;
    List<Constructible> availableBlocks = new ArrayList<>(EnumSet.allOf(Constructible.class));

    public ChooseBlockPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate);
        blockPredicate = phasePredicate;
    }

    @Override
    public void stateInit() {
        nextPhase = new ChooseWorkerPhase(currentGame, null);
        turnPlayer = currentGame.getTurnPlayer();

        availableBlocks = availableBlocks
                .stream()
                .filter(block -> blockPredicate.test(turnPlayer, block))
                .collect(Collectors.toList());

        if (availableBlocks.size() > 1) {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the constructible to build"));

            currentGame.notifyTurnPlayer(new AvailableChoicesUpdate(stringify(availableBlocks)));
        }
        else
            try {
                currentGame.runPhase(availableBlocks.get(0).toString());
            } catch (Exception e) {
                //Never thrown since the passed string is well-formatted
            }
    }

    @Override
    public void run(String arg) throws InvalidSelectionException {
        PlayerState turnPlayerState = turnPlayer.getPlayerState();
        parseArg(arg);

        Constructible selectedConstructible = Constructible.valueOf(arg);

        turnPlayerState.setSelectedConstructible(selectedConstructible);

        turnPlayerState.getSelectedWorker().build(turnPlayerState.getSelectedCell(), turnPlayerState.getSelectedConstructible());
        turnPlayerState.getSelectedWorker().getOldBuildPositions().add(turnPlayerState.getSelectedCell()); //TODO Should we do this here or in build?

        checkWinConditions();
    }

    private void parseArg(String arg) throws InvalidSelectionException {
        if (stringify(availableBlocks).contains(arg))
            return;
        throw new InvalidSelectionException("The specified block is not available, try with a different one");
    }
}
