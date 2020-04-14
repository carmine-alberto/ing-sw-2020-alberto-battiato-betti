package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.ChallengerSelectionController;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.MessageBox;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ViewEventHandler implements Runnable {
    private ObjectInputStream in;
    private Client client;

    ViewEventHandler(Client client, ObjectInputStream in) throws IOException {

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

    public void handle(ChangeViewEvent event) {
        client.getViewState().next(event.viewState);
        System.out.println(client.getViewState().getClass());
    }

    public void handle(ChoseActionEvent event) {
        client.getViewState().render();
        client.getViewState().showMessage("Scegli quale azione eseguire");
        //todo mandare azioni disponibili
        System.out.println(client.getViewState().getClass());

    }

    public void handle(ChoseWorkerEvent event) {
        System.out.println("We remind you that your color is" + callee.getColour());

        client.getViewState().showMessage("Select the worker you want to move (enter its coordinates: (x, y))");
        //todo mandare azioni disponibili
        System.out.println(client.getViewState().getClass());
    }


    private void accept(Event receivedEvent) {
        receivedEvent.visit(this);
    }



    public void handle(WarningEvent event) {
        Platform.runLater(() -> MessageBox.show(event.message, "Warning"));
    }

    public void handle(SelectedGodsEvent selectedGodsEvent) {
        Platform.runLater(() -> {
            ((GodPowerView) client.getViewState()).setGodsList(selectedGodsEvent.godPowers);
            client.getViewState().render();
        });
    }

    public void handle(AvailableGodsEvent availableGodsEvent) {
        Platform.runLater(() -> {
            ((ChallengerSelectionView) client.getViewState()).setGodsList((availableGodsEvent.godPowers));
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
        Platform.runLater(() -> {
            ((BoardView)client.getViewState()).setHideColorPickerBox(true);
            client.getViewState().render();
            MessageBox.show(update.message, "Notification");
        });
    }
}
