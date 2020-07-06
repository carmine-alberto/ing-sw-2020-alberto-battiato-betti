package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.viewUtility.ChoiceWindow;
import it.polimi.ingsw.view.viewUtility.MessageWindow;
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

    /**
     * Shows the current viewState.
     * Created implementing a pseudo-Template Method pattern
     */
    @Override
    public void render() {
        Platform.runLater(this::fXRender);
    }

    /**
     * Part of the Template Method pattern - must be implemented by subclasses to define the rendering behaviour
     */
    protected abstract void fXRender();

    /**
     * Fills the cell using data contained in the corresponding fieldCell
     * @param cell The GUI component to be filled
     * @param fieldCell The rep component to draw data from
     */
    protected void fillCell(StackPane cell, FieldCell fieldCell) {
        final Color BLOCK_COLOR = Color.web("#F0DC7E");
        final Double BASE_SCALING_FACTOR = 0.80;
        final Double RECURSIVE_SCALING_FACTOR = 0.86;
        final Integer DIAMETER_OVER_RADIUS = 2;

        Double scalingFactor = BASE_SCALING_FACTOR;

        Rectangle block;
        for (Integer i = 0; i < fieldCell.getHeight(); i++) {
            block = new Rectangle(scalingFactor * cell.getPrefWidth(), scalingFactor * cell.getPrefWidth());
            block.heightProperty().bind(cell.heightProperty().multiply(scalingFactor));
            block.widthProperty().bind(block.heightProperty());
            block.setFill(BLOCK_COLOR);
            block.setStroke(Color.BLACK);

            cell.getChildren().add(block);

            scalingFactor *= RECURSIVE_SCALING_FACTOR;
        }

        if (fieldCell.getHasDome()) {
            Double radius = scalingFactor * cell.getPrefWidth() / DIAMETER_OVER_RADIUS;
            Circle dome = new Circle(radius, Color.BLUE);
            dome.radiusProperty().bind(cell.heightProperty().multiply(scalingFactor / DIAMETER_OVER_RADIUS));
            cell.getChildren().add(dome);
            cell.setAlignment(dome, Pos.CENTER);
        }

        if (fieldCell.getWorker() != null) { //TODO Worker should look better than a plain circle
            Double radius = scalingFactor * cell.getPrefWidth() / 2;
            Circle worker = new Circle(radius, Color.valueOf(fieldCell
                    .getWorker()
                    .getOwner()
                    .getColour()
                    .toUpperCase()));
            worker.radiusProperty().bind(cell.heightProperty().multiply(scalingFactor / DIAMETER_OVER_RADIUS));
            cell.getChildren().add(worker);
            cell.setAlignment(worker, Pos.CENTER);
        }
    }

    @Override
    protected void connectionClosedHandler() {
       terminate("The server is no longer available - The game will be terminated!");
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
