package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuildPhase extends TurnPhase {
    List<FieldCell> availableCells = new ArrayList<>();

    public BuildPhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    public void stateInit() {
        this.nextPhase = new ChooseBlockPhase(currentGame);

    }

    @Override
    public void run(String arg) {
        Player turnPlayer = currentGame.getTurnPlayer();

        List<FieldCell> adjacentCellsPlusSelf = turnPlayer.getPlayerState().getSelectedCell()
                                                          .getAdjacentCells();
        adjacentCellsPlusSelf.add(turnPlayer.getPlayerState().getSelectedCell());  //Passing adjacent cells + the cell I'm on, for Zeus

        availableCells = adjacentCellsPlusSelf
                .stream()
                .filter(adjacentCell -> turnPlayer
                        .getBuildPredicate()
                        .test(adjacentCell, turnPlayer.getPlayerState().getSelectedWorker())
                       )
                .collect(Collectors.toList());

        if (availableCells.isEmpty()) { //TODO Currently, the player loses even if the second worker can build. Should we rollback to the ChooseWorkerPhase()?
            currentGame.removeTurnPlayer();
            setNextPhase(new ChooseWorkerPhase(currentGame));
            currentGame.setNextTurnPlayer();
        }

        //TODO Send notification to the turnPlayer
        //TODO Wait for response

        FieldCell destinationCell = turnPlayer.getPlayerState().getSelectedCell();
        if (!availableCells.contains(destinationCell)) {
            //TODO Send notification of illegal move and let the player choose a valid cell repeating the phase - we expect this branch to be taken only by cheaters, a minority, so no Flyweight pattern is used
            setNextPhase(this);
        }
    }




    @Override
    public void stateEnd() {

    }
}
