package it.polimi.ingsw.phases;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.animation.KeyFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovePhase extends TurnPhase {
    List<FieldCell> availableCells = new ArrayList<>();

    public MovePhase(Game currentGame) {
        super(currentGame);
    }

    @Override
    protected void stateInit() {
        nextPhase = new BuildPhase(currentGame);

    }

    @Override
    protected void run() {
        Player turnPlayer = currentGame.getTurnPlayer();

        availableCells = turnPlayer
                .getSelectedWorker()
                .getAdjacentCells()
                .stream()
                .filter(adjacentCell -> turnPlayer
                        .getMovePredicate()
                        .test(adjacentCell, turnPlayer.getSelectedWorker()))
                .collect(Collectors.toList());

        if (availableCells.isEmpty()) {
            currentGame.removeTurnPlayer();
            setNextPhase(new ChooseActionPhase(currentGame));
            currentGame.setNextTurnPlayer();
        }

        //Se la lista è vuota, il giocatore viene eliminato e chiamo setNextPhase(new ChooseActionPhase()) + setNextPlayer()
        //Invio la notifica al giocatore con la lista
        //Mi metto in attesa della risposta
        //Chiamo la move sul worker

    }

    @Override
    protected void stateEnd() {
        //Se ho un predicato che agisce a fine turno -> eseguo una determinata azione

    }
}
