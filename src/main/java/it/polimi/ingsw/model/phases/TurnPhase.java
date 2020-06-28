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
     * This function is used to start the phase and set the phasePredicate opportunely
     */
    public abstract void stateInit();

    /**
     * This function is called to get the turnPhase running
     *
     * @param arg The string used in the Phase
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection is invalid
     */
    public abstract void run(String arg) throws IllegalFormatException, InvalidSelectionException;

    /**
     * This function is called to end the phase
     */
    public void stateEnd() {
        currentGame.getPlayers().forEach(this::checkIsWinner);
        turnPlayer.getSelectedGod().setNextPhase(currentGame);
        //TODO Send notifications - not needed though

    }

    /**
     * this function checks the players' specific Win Conditions
     */
    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(this::checkPlayerWinConditions);
    }

    /**
     * this method is used to check the validity of the user Input
     * @param arg the coordinates
     * @throws IllegalFormatException if the format is not recognised
     */
    protected void parseCoordinatesArg(String arg) throws IllegalFormatException {
        if (arg.length() == THREE) {
            try {
                Integer x = Integer.parseInt(arg.substring(0, 1));
                Integer y = Integer.parseInt(arg.substring(2, 3));

                if (x <= FIELD_SIZE && x > ZERO && y <= FIELD_SIZE && y > ZERO)
                    return;
            } catch (NumberFormatException e) {
                throw new IllegalFormatException("Your selection's format was not recognized; try again");
            }
        }
        throw new IllegalFormatException("Your selection's format was not recognized; try again");
    }

    /**
     * this method is called when the player has no available actions left
     */
    protected void removeTurnPlayerFromGame() {
        currentGame.removeTurnPlayer();
    }

    /**
     * this method is used to convert the given list into a list of actions
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
     * this method converts the given user input into the model's coordinates system
     * @param coordinates given from user
     * @return the recognised coordinates
     * @throws InvalidSelectionException if the selection is out of bound
     */
    protected FieldCell extractCellFromCoordinates(String coordinates) throws InvalidSelectionException {
        Integer x = Integer.parseInt(coordinates.substring(FIRST_ELEMENT, ONE));
        Integer y = Integer.parseInt(coordinates.substring(TWO, THREE));

        return currentGame.getCell(x - CORRECTION, y - CORRECTION);

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
