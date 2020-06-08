package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


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
     * This function is used to start the phase
     */
    public abstract void stateInit();

    /**
     * This function is called to get the turnPhase running
     *
     * @param arg The string used in the Phase
     * @throws IllegalFormatException If the format of the string does not fit the required one
     * @throws InvalidSelectionException If the selection of the action is invalid
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

    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(this::checkPlayerWinConditions);
    }

    protected void parseCoordinatesArg(String arg) throws IllegalFormatException {
        if (arg.length() == 3) {
            try {
                Integer x = Integer.parseInt(arg.substring(0, 1));
                Integer y = Integer.parseInt(arg.substring(2, 3));

                if (x < 6 && x > 0 && y < 6 && y > 0)
                    return;
            } catch (NumberFormatException e) {
                throw new IllegalFormatException("Your selection's format was not recognized; try again");
            }
        }
        throw new IllegalFormatException("Your selection's format was not recognized; try again");
    }

    protected void removeTurnPlayerFromGame() {
        currentGame.removeTurnPlayer();
    }

    protected List<String> stringify(List<?> availableEnumItems) {
        return availableEnumItems
                .stream()
                .map(action -> action.toString())
                .collect(Collectors.toList());
    }

    protected FieldCell extractCellFromCoordinates(String coordinates) throws InvalidSelectionException {
        Integer x = Integer.parseInt(coordinates.substring(0, 1));
        Integer y = Integer.parseInt(coordinates.substring(2, 3));

        return currentGame.getCell(x-1, y-1);

    }


    private void checkPlayerWinConditions(Player player) {
        BiPredicate winConditions = player
            .getOpponents()
            .stream()
            .map(opponent -> opponent
                    .getSelectedGod()
                    .getOuterPredicate("winCondition"))
            .filter(Objects::nonNull)
            .reduce(player.getWinConditions(), BiPredicate::and);

        if (winConditions.test(currentGame, player.getPlayerState().getSelectedWorker()))
            player.setIsWinner(true); //CON LAMBDA


        /*player.getOpponents().forEach(opponent -> {
            if(opponent.getSelectedGod().getOuterPredicate("winCondition") != null) {
                if (winConditions.and(opponent.getSelectedGod().getOuterPredicate("winCondition")).test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
            } else
                if (winConditions.test(currentGame, player.getPlayerState().getSelectedWorker()))
                    player.setIsWinner(true);
        }); SENZA LAMBDA  */
    }

    private void checkIsWinner(Player player) {
        if (player.getIsWinner()) {
            currentGame.endGame();
        }
    }
}
