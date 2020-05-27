package it.polimi.ingsw.cview;

import it.polimi.ingsw.Client;
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
    private Client client;
    static Float TIMEOUT = 1F;

    public ViewEventHandler(Client client, ObjectInputStream in) throws IOException {
        this.client = client;
        this.in = in;
        System.out.println("Thread created");

    }

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
        client.getViewState().next(event.viewState);
        System.out.println(client.getViewState().getClass());
    }

    public void handle(WarningEvent event) {
        client.getViewState().showWarning(event.message);
    }

    public void handle(SelectedGodsEvent selectedGodsEvent) {
        Platform.runLater(() -> {
            client.setAvailableGods(selectedGodsEvent.godPowers);
            client.getViewState().render();
        });
    }

    public void handle(AvailableGodsEvent availableGodsEvent) {
        Platform.runLater(() -> {
            client.setAvailableGods((availableGodsEvent.godPowers));
            client.getViewState().render();
        });
    }

    public void handle(BoardUpdate boardUpdate) {
        Platform.runLater(() -> {
            client.setBoard(boardUpdate.board);
            client.getViewState().render();
        });
    }

    public void handle(PhaseUpdate update) {
        client.setAvailableCellsX(new ArrayList<>());  //At every phase change, the available cells are reset
        client.setAvailableCellsY(new ArrayList<>());
        client.getViewState().render();

        client.getViewState().showMessage(update.message);

    }

    public void handle(GameStartedEvent gameStartedEvent) {
        Platform.runLater(() -> {
            client.getViewState().next("BoardView");
            client.getViewState().render();
        });
    }

    public void handle(AvailableChoicesUpdate update) {
        client.getViewState().showChoices(update.availableChoices);

    }

    public void handle(AvailableCellsUpdate availableCellsUpdate) {
        client.setAvailableCellsX(availableCellsUpdate.xCoordinates);
        client.setAvailableCellsY(availableCellsUpdate.yCoordinates);

        client.getViewState().render();

    }

    public void handle(PlayerLostUpdate update) {
        client.getViewState().showMessage(update.losingPlayerNickname + " has no available moves and was removed from the game");
    }

    public void handle(GameEndUpdate update) {
        client.getViewState().showMessage("The game is over. " +
                update.winnerNickname + " is the winner!");
        client.getViewState().terminate();
    }

    public void handle(PingEvent pingEvent) {
        if (client.getPingTimestamp() == null) {
            client.setPingTimestamp(LocalDateTime.now());
            startDeltaTimestampCheckingThread();
        }
        client.setPingTimestamp(LocalDateTime.now());
        client.getViewState().notify(new PingEvent());  //TODO We should decouple pinging and JavaFX thread
    }

    public void handle(GameInformationEvent gameInformationsEvent) {
        client.setPlayerInfos(gameInformationsEvent.playersName, gameInformationsEvent.chosenGods, gameInformationsEvent.chosenColor);
    }

    private void startDeltaTimestampCheckingThread() {
        Float scalingFactor = 1.5F;
        new Thread(() -> {
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                if (SECONDS.between(client.getPingTimestamp(), now) > scalingFactor * TIMEOUT) { //TODO Should we make all of this synchronized?
                    client.getViewState().showMessage("The server is no longer available - The current game will be terminated");
                    client.getViewState().terminate();
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
