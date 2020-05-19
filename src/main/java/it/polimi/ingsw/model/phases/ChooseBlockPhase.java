package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableChoicesUpdate;
import it.polimi.ingsw.controller.events.PhaseUpdate;
import it.polimi.ingsw.model.Constructible;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ChooseBlockPhase extends TurnPhase {
    private List<Constructible> availableBlocks;

    public ChooseBlockPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "blockPredicate");

    }

    @Override
    public void stateInit() {

        availableBlocks = new ArrayList<>(EnumSet.allOf(Constructible.class))
                .stream()
                .filter(block -> phasePredicate.test(turnPlayer, block))
                .collect(Collectors.toList());

        if (availableBlocks.size() > 1) {
            currentGame.notifyTurnPlayer(new PhaseUpdate("Select the constructible to build"));  //TODO Fix overlapping windows

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
        turnPlayerState.setSelectedCell(null);

        checkWinConditions();
    }

    private void parseArg(String arg) throws InvalidSelectionException {
        if (stringify(availableBlocks).contains(arg))
            return;
        throw new InvalidSelectionException("The specified block is not available, try with a different one");
    }
}
