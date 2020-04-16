package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.cview.clientView.BoardView;
import it.polimi.ingsw.model.ActionEnum;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import javafx.scene.Node;

import java.util.List;
import java.util.stream.Collectors;


public abstract class TurnPhase {
    protected TurnPhase nextPhase;
    protected Game currentGame;

    public TurnPhase(Game currentGame) {
        this.currentGame = currentGame;
    }

    public abstract void stateInit();

    public abstract void run(String arg) throws IllegalFormatException, InvalidSelectionException;

    public void stateEnd() {
        currentGame.getPlayers().forEach(player -> checkIsWinner(player));
        //TODO Invio notifica

    }

    protected void checkWinConditions() {
        currentGame.getPlayers().forEach(player -> checkPlayerWinConditions(player));
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
        setNextPhase(new ChooseWorkerPhase(currentGame));
        currentGame.setNextTurnPlayer();
        currentGame.endPhase();
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
        if (player.getWinConditions().test(currentGame, player.getPlayerState().getSelectedWorker()))
            player.setIsWinner(true);
    }

    private void checkIsWinner(Player player) {
        if (player.getIsWinner()) {
            currentGame.endGame();
        }
    }


    public TurnPhase getNextPhase() {
        return this.nextPhase;
    }

    public void setNextPhase(TurnPhase nextTurnPhase) {
        this.nextPhase = nextTurnPhase;
    }

}
