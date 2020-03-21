package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Move implements Action {
    Player turnPlayer;
    List<GameWorker> turnPlayerWorkers;
    @Override
    public Boolean isLegal() {
        //turnPlayer = game.getTurnPlayer();
        turnPlayerWorkers = turnPlayer.getWorkers();
        for (GameWorker worker: turnPlayerWorkers) {
            //Se esiste una cella vicina al worker disponibile, ritorno true
        }

        return true; //TODO Eliminare return
    }

    @Override
    public void run() {
        List<GameWorker> availableWorkers = new ArrayList<>();

        // TODO: Inserire le due liste di celle disponibili in una mappa
        List<FieldCell> availableCellsWorker1 = turnPlayerWorkers.get(0).getAvailableCells();
        List<FieldCell> availableCellsWorker2 = turnPlayerWorkers.get(1).getAvailableCells();


        if (!availableCellsWorker1.isEmpty())
            availableWorkers.add(turnPlayerWorkers.get(0));
        if (!availableCellsWorker2.isEmpty())
            availableWorkers.add(turnPlayerWorkers.get(1));

        //Chiedo all'utente quale worker vuole muovere

        //turnPlayer.getView().notify(new SelectWorkerEvent(availableWorkers));

        CompletableFuture<String> chosenWorkerResponse;

        //turnPlayer.getView().notify(new SelectDestinationCellEvent(workersMap.get(chosenWorkerResponse)));

        //Muovo il worker selezionato nella cella selezionata
        //TODO Controllare la legalità delle azioni nel caso in cui si utilizzi una CLI
    }
}
