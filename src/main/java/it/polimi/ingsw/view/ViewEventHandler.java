package it.polimi.ingsw.view;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;


import static it.polimi.ingsw.GameSettings.GRANULARITY;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ViewEventHandler implements Runnable {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private View view;
    static Float TIMEOUT = 1F;

    public ViewEventHandler(View view, ObjectInputStream in) throws IOException {
        this.view = view;
        this.in = in;
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

    public void handle(ChangeViewEvent event) { ;
        ReceivedViewState receivedEvent = viewStateNameToEnum(event.viewState);
        view.getViewState().next(receivedEvent);
    }

    public void handle(WarningEvent event) {
        view.getViewState().showWarning(event.message);
    }

    public void handle(SelectedGodsUpdate selectedGodsUpdate) {
        Platform.runLater(() -> {
            view.setAvailableGods(selectedGodsUpdate.godPowers);
            view.getViewState().render();
        });
    }

    public void handle(AvailableGodsUpdate availableGodsEvent) {
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
            view.getViewState().next(ReceivedViewState.BOARD_VIEW_STATE);
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
        view.getViewState().terminate("The game is over. " +
                update.winnerNickname + " is the winner!");
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
            view.setGameOver(false);
            while (!view.isGameOver()) {
                LocalDateTime now = LocalDateTime.now();
                if (SECONDS.between(view.getPingTimestamp(), now) > scalingFactor * TIMEOUT) { //TODO Should we make all of this synchronized?
                    view.getViewState().terminate("The server is no longer available - The connection will be closed");
                }
                try {
                    Thread.sleep((long) (scalingFactor * TIMEOUT * GRANULARITY));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Placed here for synchronization purposes
     * @param terminationEvent sent by the server
     */
    public void handle(TerminationEvent terminationEvent) {
    }

    public void handle(ClientDisconnectedEvent clientDisconnectedEvent) {
        view.getViewState().terminate("Somebody was disconnected from the server - The current game has been terminated");
    }

    private ReceivedViewState viewStateNameToEnum(String event){
        event = event
                .replaceAll("(.)([A-Z])", "$1_$2")
                .toUpperCase();
        return ReceivedViewState.valueOf(event);
    }
}
