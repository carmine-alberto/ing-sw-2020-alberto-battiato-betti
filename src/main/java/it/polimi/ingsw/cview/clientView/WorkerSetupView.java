package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.cview.clientView.WorkerSetupView.CellCoordinate.Y;
import static it.polimi.ingsw.cview.clientView.WorkerSetupView.CellCoordinate.X;

public class WorkerSetupView extends GUIView {
    enum CellCoordinate {
        X, Y;
    }

    final Integer edgeTolerance = 20;
    final Integer BOARD_SIZE = 5;
    final Integer NUM_OF_WORKERS = 2;

    private ColorPicker colorPicker;
    private TilePane board;

    public WorkerSetupView(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    protected void fXRender() {
        FieldCell[][] boardRep = client.getBoard();
        StackPane cell;

        if (boardRep != null) {
            colorPicker = new ColorPicker();
            colorPicker.setValue(Color.ORANGE);

            Button confirmSelections = new Button("Confirm");
            confirmSelections.setOnAction(e -> handleConfirmation((Button) e.getSource()));

            HBox colorPickerBox = new HBox(10, colorPicker, confirmSelections);

            board = new TilePane();
            FloatProperty tileSideLength = new SimpleFloatProperty(Math.min((float) mainStage.getScene().getWidth() / 5 - edgeTolerance, (float) mainStage.getScene().getHeight() / 5 - edgeTolerance));
            mainStage.getScene().heightProperty().addListener(e -> updateProperty(tileSideLength));
            mainStage.getScene().widthProperty().addListener(e -> updateProperty(tileSideLength));

            board.setOrientation(Orientation.HORIZONTAL);
            board.setPrefRows(5);
            board.setPrefColumns(5);
            board.prefTileWidthProperty().bind(tileSideLength);
            board.prefTileHeightProperty().bind(tileSideLength);

            for (Integer i = 1; i < 6; i++)
                for (Integer j = 1; j < 6; j++) {
                    cell = new StackPane();
                    cell.setId(i.toString() + " " + j.toString());
                    cell.setBackground(new Background(new BackgroundFill(Color.web("#41FA0E", 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
                    cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    cell.setOnMouseClicked(e -> handleCellClick((StackPane) e.getSource()));
                    cell.prefHeightProperty().bind(tileSideLength);
                    cell.prefWidthProperty().bind(tileSideLength);

                    fillCell(cell, boardRep[i - 1][j - 1]);

                    board.getChildren().add(cell);
                }

            VBox interfaceBox;
            interfaceBox = new VBox(20, board);
            interfaceBox.setPadding(new Insets(20));
            interfaceBox.getChildren().add(0, colorPickerBox);
            interfaceBox.setAlignment(Pos.CENTER);
            interfaceBox.setFillWidth(false);

            mainStage.getScene().setRoot(interfaceBox);
        }
    }

    private void handleConfirmation(Button source) {
        String color = colorPicker.getValue().toString();

        for (Integer i = 0; i < BOARD_SIZE; i++)
            for (Integer j = 0; j < BOARD_SIZE; j++)
                if(client.getBoard()[i][j].getWorker() != null && client.getBoard()[i][j].getWorker().getOwner().getColour().equals(colorPicker.getValue().toString())) {
                    this.showMessage("One of your opponents already chose this color, pick another one!");
                    return;
                }

        if (Math.toIntExact(board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane) cell).getBorder().getStrokes().get(0).getLeftStroke().equals(Color.RED))
                .count()) != NUM_OF_WORKERS) {
            this.showMessage("Enter the position of the 2 workers");
            return;
        }

        List<Integer> xCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(Color.RED))
                .map(cell -> extractCellCoordinate(X, cell))
                .collect(Collectors.toList());

        List<Integer> yCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(Color.RED))
                .map(cell -> extractCellCoordinate(Y, cell))
                .collect(Collectors.toList());

        if(!client.getBoard()[xCoordinates.get(0) - 1][yCoordinates.get(0) - 1].isFree() || !client.getBoard()[xCoordinates.get(1) - 1][yCoordinates.get(1) - 1].isFree()) {
            this.showMessage("You can't place a worker in a cell that is already occupied");
            return;
        }

        board
        .getChildren()
        .forEach(cell ->
                ((StackPane)cell).setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));


        notify(new WorkerSelectionEvent(xCoordinates, yCoordinates, color));
    }

    private Integer extractCellCoordinate(WorkerSetupView.CellCoordinate coord, Node cell) {
        return switch (coord) {
            case X -> Integer.parseInt(cell.getId().substring(0, 1));
            case Y -> Integer.parseInt(cell.getId().substring(2, 3));
        };
    }

    private void fillCell(StackPane cell, FieldCell fieldCell) {
        List<Integer> availableXCoordinates = client.getAvailableCellsX();
        List<Integer> availableYCoordinates = client.getAvailableCellsY();

        Double baseWidth = 0.9 * cell.getPrefWidth();

        Rectangle block;

        for (Integer i = 0; i < fieldCell.getHeight(); i++) {
            block = new Rectangle(baseWidth, baseWidth);
            block.setFill(Color.web("#F0DC7E"));
            block.setStroke(Color.BLACK);

            cell.getChildren().add(block);

            baseWidth *= 0.8;
        }

        if(fieldCell.getHasDome()){
            Circle dome = new Circle(baseWidth/2, Color.BLUE);
            cell.getChildren().add(dome);
            cell.setAlignment(dome, Pos.CENTER);
        }

        if (fieldCell.getWorker() != null) { //TODO Worker should look better than a plain circle
            Circle worker = new Circle(baseWidth/2, Color.valueOf(fieldCell
                    .getWorker()
                    .getOwner()
                    .getColour()
                    .toUpperCase()));
            cell.getChildren().add(worker);
            cell.setAlignment(worker, Pos.CENTER);
        }

    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/5 - edgeTolerance,(float)mainStage.getScene().getHeight()/5 - edgeTolerance));
    }

    private void handleCellClick(StackPane clickedCell) {
        toggleCell(clickedCell);
    }

    private void toggleCell(StackPane clickedCell) {
        if (clickedCell.getBorder().getStrokes().get(0).getLeftStroke().equals(Color.BLACK)) {
            clickedCell.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            clickedCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }
}
