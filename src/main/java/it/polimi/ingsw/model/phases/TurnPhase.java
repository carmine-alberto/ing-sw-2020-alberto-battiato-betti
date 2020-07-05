package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GameSettings.*;


public abstract class TurnPhase {
    protected Game currentGame;
    protected Player turnPlayer;
    protected BiPredicate phasePredicate;


    public TurnPhase(Game currentGame){
        this.currentGame = currentGame;
        this.turnPlayer = currentGame.getTurnPlayer();
    }

    public TurnPhase(Game currentGame, BiPredicate phasePredicate, String predicateType) {
        this(currentGame);
        this.phasePredicate = turnPlayer
            .getOpponents()
            .stream()
            .map(opponent -> opponent
                    .getSelectedGod()
                    .getOuterPredicate(predicateType))
            .filter(Objects::nonNull)
            .reduce(phasePredicate, BiPredicate::and);
    }

    /**
     * Function used to calculate availableObjects or send notifications to the turnPlayer
     */
    public abstract void stateInit();

    /**
     * This function is called to get the turnPhase running
     *
     * @param arg The user selection, varying depending on the Phase
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection is invalid
     */
    public abstract void run(String arg) throws IllegalFormatException, InvalidSelectionException;

    /**
     * Checks the winCondition for each player and sets the next phase
     */
    public void stateEnd() {
        currentGame.getPlayers().forEach(this::checkIsWinner);
        turnPlayer.getSelectedGod().setNextPhase(currentGame);
    }

    /**
     * Checks player-specific winConditions
     */
    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(this::checkPlayerWinConditions);
    }

    /**
     * Checks the validity of the user-input coordinates
     * @param arg The coordinates
     * @throws IllegalFormatException if the format is not recognised
     */
    protected void parseCoordinatesArg(String arg) throws IllegalFormatException {
        if (arg.length() == EXPECTED_LENGTH) {
            try {
                Integer x = Integer.parseInt(arg.substring(0, 1));
                Integer y = Integer.parseInt(arg.substring(2, 3));

                if (x <= FIELD_SIZE && x > LOWER_BOUND && y <= FIELD_SIZE && y > LOWER_BOUND)
                    return;
            } catch (NumberFormatException e) {
                throw new IllegalFormatException("Your selection's format was not recognized; try again");
            }
        }
        throw new IllegalFormatException("Your selection's format was not recognized; try again");
    }

    /**
     * Removes the player from the game. Called when they have no actions left
     */
    protected void removeTurnPlayerFromGame() {
        currentGame.removeTurnPlayer();
    }

    /**
     * Converts a list of enums into a list of strings
     * @param availableEnumItems Enum list
     * @return converted list
     */
    protected List<String> stringify(List<?> availableEnumItems) {
        return availableEnumItems
                .stream()
                .map(action -> action.toString())
                .collect(Collectors.toList());
    }

    /**
     * Extracts the game cell matching the passed coordinates
     * @param coordinates given from user
     * @return the recognised coordinates
     */
    protected FieldCell extractCellFromCoordinates(String coordinates)  {
        Integer x = Integer.parseInt(coordinates.substring(FIRST_ELEMENT_INDEX, X_STARTING_POSITION));
        Integer y = Integer.parseInt(coordinates.substring(Y_STARTING_POSITION, EXPECTED_LENGTH));

        return currentGame.getCell(x - OFFSET, y - OFFSET);

    }


    private void checkPlayerWinConditions(Player player) {
        //WITH LAMBDA

        BiPredicate winConditions = player
            .getOpponents()
            .stream()
            .map(opponent -> opponent
                    .getSelectedGod()
                    .getOuterPredicate("winCondition"))
            .filter(Objects::nonNull)
            .reduce(player.getWinConditions(), BiPredicate::and);

        if (winConditions.test(currentGame, player.getPlayerState().getSelectedWorker()))
            player.setIsWinner(true);


        /*WITHOUT LAMBDA
        player.getOpponents().forEach(opponent -> {
            if(opponent.getSelectedGod().getOuterPredicate("winCondition") != null) {
                if (winConditions.and(opponent.getSelectedGod().getOuterPredicate("winCondition")).test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
            } else
                if (winConditions.test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
        }); */
    }

    private void checkIsWinner(Player player) {
        if (player.getIsWinner()) {
            currentGame.endGame();
        }
    }
}
