package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.utility.ChoiceWindow;
import it.polimi.ingsw.view.utility.MessageWindow;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
        Platform.runLater(this::fXRender);
    }

    protected abstract void fXRender();

    protected void fillCell(StackPane cell, FieldCell fieldCell) {
        final Double BASE_SCALING_FACTOR = 0.82;
        final Double RECURSIVE_SCALING_FACTOR = 0.85;

        Double baseWidth = BASE_SCALING_FACTOR * cell.getPrefWidth();

        Rectangle block;

        for (Integer i = 0; i < fieldCell.getHeight(); i++) {
            block = new Rectangle(baseWidth, baseWidth);
            block.setFill(Color.web("#F0DC7E"));
            block.setStroke(Color.BLACK);

            cell.getChildren().add(block);

            baseWidth *= RECURSIVE_SCALING_FACTOR;
        }

        if (fieldCell.getHasDome()) {
            Circle dome = new Circle(baseWidth / 2, Color.BLUE);
            cell.getChildren().add(dome);
            cell.setAlignment(dome, Pos.CENTER);
        }

        if (fieldCell.getWorker() != null) { //TODO Worker should look better than a plain circle
            Circle worker = new Circle(baseWidth / 2, Color.valueOf(fieldCell
                    .getWorker()
                    .getOwner()
                    .getColour()
                    .toUpperCase()));
            cell.getChildren().add(worker);
            cell.setAlignment(worker, Pos.CENTER);
        }
    }

    @Override
    protected void connectionClosedHandler() {
       terminate("The server is no longer available - The connection will be closed!");
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
    public void terminate(String reason) {
        super.terminate(reason);
        Platform.runLater(() -> {
            MessageWindow.show(reason, "Warning");
            Platform.exit();
            System.exit(0);
        });
    }
}
