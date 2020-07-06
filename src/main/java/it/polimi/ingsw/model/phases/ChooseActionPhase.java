package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.controller.events.AvailableChoicesUpdate;
import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utility.GameSettings.FIRST_ELEMENT_INDEX;
import static it.polimi.ingsw.utility.GameSettings.X_STARTING_POSITION;

public class ChooseActionPhase extends TurnPhase {
    private List<ActionEnum> availableActions;

    public ChooseActionPhase(Game currentGame, BiPredicate phasePredicate) {
        super(currentGame, phasePredicate, "actionPredicate");
    }

    /**
     * Calculates the available actions and, if greater than 1 in quantity, sends them to the turnPlayer
     */
    @Override
    public void stateInit() {

        availableActions = new ArrayList<>(EnumSet.allOf(ActionEnum.class))
                .stream()
                .filter(action -> phasePredicate.test(action, turnPlayer))
                .collect(Collectors.toList());

        if (availableActions.size() > X_STARTING_POSITION) {

            currentGame.notifyTurnPlayer(new AvailableChoicesUpdate(stringify(availableActions)));
        }
        else
            try {
                currentGame.runPhase(availableActions.get(FIRST_ELEMENT_INDEX).toString());
            } catch (Exception e) {
                //Never thrown since the passed string is well-formatted
            }
    }

    /**
     * Sets the player's selected action
     *
     * @param arg The selected action
     * @throws InvalidSelectionException if the selected action is among the available actions
     */
    @Override
    public void run(String arg) throws InvalidSelectionException {
        validateArg(arg);

        ActionEnum selectedAction = ActionEnum.valueOf(arg);

        turnPlayer.getPlayerState().setSelectedAction(selectedAction);
    }

    /**
     * This method is used to check the validity of the received action
     * @param arg The action to validate
     * @throws InvalidSelectionException if the action is not among the available actions
     */
    private void validateArg(String arg) throws InvalidSelectionException {
        if (stringify(availableActions).contains(arg))
            return;
        throw new InvalidSelectionException("The specified action is not available, try with a different one");
    }

}
