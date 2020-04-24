package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableChoicesUpdate;
import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.predicates.actionPredicates.ActionPredicate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ChooseActionPhase extends TurnPhase {
    List<ActionEnum> availableActions;
    private BiPredicate<ActionEnum, Player> actionPredicate;
    Player turnPlayer;

    public ChooseActionPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate);
        availableActions = new ArrayList(EnumSet.allOf(ActionEnum.class));
        actionPredicate = phasePredicate;
    }

    @Override
    public void stateInit() {
        turnPlayer = currentGame.getTurnPlayer();

        availableActions = availableActions
                .stream()
                .filter(action -> actionPredicate.test(action, turnPlayer))
                .collect(Collectors.toList());

        if (availableActions.size() > 1) {
            //currentGame.notifyTurnPlayer(new PhaseUpdate("Select the action to perform")); //TODO Fix MessageBox overlapping with ChoiceBox

            currentGame.notifyTurnPlayer(new AvailableChoicesUpdate(stringify(availableActions)));
        }
        else
            try {
                currentGame.runPhase(availableActions.get(0).toString());
            } catch (Exception e) {
                //Never thrown since the passed string is well-formatted
            }
    }

    @Override
    public void run(String arg) throws InvalidSelectionException {
        parseArg(arg);

        ActionEnum selectedAction = ActionEnum.valueOf(arg);

        turnPlayer.getPlayerState().setSelectedAction(selectedAction);
    }

    private void parseArg(String arg) throws InvalidSelectionException {
        if (stringify(availableActions).contains(arg))
            return;
        throw new InvalidSelectionException("The specified action is not available, try with a different one");
    }

}
