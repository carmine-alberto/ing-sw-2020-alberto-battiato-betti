package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.cview.ViewState;
import it.polimi.ingsw.cview.utility.ChoiceWindow;
import it.polimi.ingsw.cview.utility.MessageWindow;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public abstract class GUIViewState extends ViewState {

    public GUIViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    @Override
    public void render() {
        Platform.runLater(this::
                fXRender);
    }

    protected abstract void fXRender();

    @Override
    protected void connectionClosedHandler() {
        MessageWindow.show("Connection closed - Restart the client and try again!", "Connection Error");
        terminate();
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            MessageWindow.show(message, "Notification");
        });
    }

    @Override
    public void showWarning(String message) {
        Platform.runLater(() -> {
            MessageWindow.show(message, "Warning");
        });
    }

    @Override
    public void showChoices(List<String> availableChoices) {
        Platform.runLater(() -> {
            String choice = ChoiceWindow.show("Available items", availableChoices );
            notify(new UserInputEvent(choice));
        });
    }


    @Override
    public void terminate() {
        Platform.runLater(() -> this.mainStage.close());
        Platform.exit();
    }
}
