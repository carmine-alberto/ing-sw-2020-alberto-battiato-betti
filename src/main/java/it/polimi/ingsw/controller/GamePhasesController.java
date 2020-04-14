package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.GameWorker;
import it.polimi.ingsw.model.Player;

public class GamePhasesController extends ControllerState {
    Player turnPlayer;
    public GamePhasesController(Controller mainController) {
        super(mainController);
        //phaseInit();
    }

    @Override
    public synchronized void handle(Event event, VirtualView view) {
        event.visit(this, view);
    }

    public void handle(WorkerSelectionEvent event, VirtualView view) {
        //Set selected worker;
        //phase.run();
        //phase.end();
        //phase.init();
        GameWorker selectedWorker;
        System.out.println("Inside GamePhasesController");
        if (isTurnPlayer(view)) {
            System.out.println("Inside if");
            turnPlayer = mainController.getCurrentGame().getTurnPlayer();
            synchronized (turnPlayer) {
                System.out.println("TP Got");
                selectedWorker = extractWorkerFromCoordinates(event);
                System.out.println("End of if");

                if (selectedWorker != null) {
                    turnPlayer.getPlayerState().setSelectedWorker(selectedWorker);
                    System.out.println("Before notify");

                    turnPlayer.setNeedToWait(false);
                    turnPlayer.notify();

                }
            }
        }
        System.out.println("End of outer if");

    }

    private GameWorker extractWorkerFromCoordinates(WorkerSelectionEvent event) {
        Integer x = event.xCoordinates.get(0);
        Integer y = event.yCoordinates.get(0);
        System.out.println(x + " " + y);

        GameWorker extractedWorker = mainController.getCurrentGame().getCell(x-1, y-1).getWorker();
        System.out.println("Worker got");

        if (extractedWorker != null) {
            System.out.println(extractedWorker.toString());

            if (!extractedWorker.getOwner().equals(mainController.getCurrentGame().getTurnPlayer()))
                return null;
        }

        return extractedWorker;

    }
}
