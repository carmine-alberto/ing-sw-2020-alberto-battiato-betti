package it.polimi.ingsw.cview;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;


import static java.time.temporal.ChronoUnit.SECONDS;

public class ViewEventHandler implements Runnable {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private View view;
    static Float TIMEOUT = 1F;

    public ViewEventHandler(View view, ObjectInputStream in) throws IOException {
        this.view = view;
        this.in = in;
        System.out.println("Thread created");

    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            try {
                Event receivedEvent = (Event) in.readObject();
                this.accept(receivedEvent);
            } catch (IOException e) {
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(Event receivedEvent) {
        receivedEvent.visit(this);
    }

    public void handle(ChangeViewEvent event) {
        view.getViewState().next(event.viewState);
        System.out.println(view.getViewState().getClass());
    }

    public void handle(WarningEvent event) {
        view.getViewState().showWarning(event.message);
    }

    public void handle(SelectedGodsEvent selectedGodsEvent) {
        Platform.runLater(() -> {
            view.setAvailableGods(selectedGodsEvent.godPowers);
            view.getViewState().render();
        });
    }

    public void handle(AvailableGodsEvent availableGodsEvent) {
        Platform.runLater(() -> {
            view.setAvailableGods((availableGodsEvent.godPowers));
            view.getViewState().render();
        });
    }

    public void handle(BoardUpdate boardUpdate) {
        Platform.runLater(() -> {
            view.setBoard(boardUpdate.board);
            view.getViewState().render();
        });
    }

    public void handle(PhaseUpdate update) {
        view.setAvailableCellsX(new ArrayList<>());  //At every phase change, the available cells are reset
        view.setAvailableCellsY(new ArrayList<>());
        view.getViewState().render();

        view.getViewState().showMessage(update.message);

    }

    public void handle(GameStartedEvent gameStartedEvent) {
        Platform.runLater(() -> {
            view.getViewState().next("BoardView");
            view.getViewState().render();
        });
    }

    public void handle(AvailableChoicesUpdate update) {
        view.getViewState().showChoices(update.availableChoices);

    }

    public void handle(AvailableCellsUpdate availableCellsUpdate) {
        view.setAvailableCellsX(availableCellsUpdate.xCoordinates);
        view.setAvailableCellsY(availableCellsUpdate.yCoordinates);

        view.getViewState().render();

    }

    public void handle(PlayerLostUpdate update) {
        view.getViewState().showMessage(update.losingPlayerNickname + " has no available moves and was removed from the game");
    }

    public void handle(GameEndUpdate update) {
        view.getViewState().showMessage("The game is over. " +
                update.winnerNickname + " is the winner!");
        view.getViewState().terminate();
    }

    public void handle(PingEvent pingEvent) {
        if (view.getPingTimestamp() == null) {
            view.setPingTimestamp(LocalDateTime.now());
            startDeltaTimestampCheckingThread();
        }
        view.setPingTimestamp(LocalDateTime.now());
        view.getViewState().notify(new PingEvent());  //TODO We should decouple pinging and JavaFX thread
    }

    public void handle(GameInformationEvent gameInformationsEvent) {
        view.setPlayerInfos(gameInformationsEvent.playersName, gameInformationsEvent.chosenGods, gameInformationsEvent.chosenColor);
    }

    private void startDeltaTimestampCheckingThread() {
        Float scalingFactor = 1.5F;
        new Thread(() -> {
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                if (SECONDS.between(view.getPingTimestamp(), now) > scalingFactor * TIMEOUT) { //TODO Should we make all of this synchronized?
                    view.getViewState().showMessage("The server is no longer available - The current game will be terminated");
                    view.getViewState().terminate();
                }
                try {
                    Thread.sleep((long) (scalingFactor * TIMEOUT * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
