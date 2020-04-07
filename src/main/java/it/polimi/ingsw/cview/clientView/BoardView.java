package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.cview.utility.MessageBox;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class BoardView extends View {
    final Integer edgeTolerance = 20;
    final Integer BOARD_SIZE = 5;

    private ChoiceBox<String> colorPicker;
    private TilePane board;

    public BoardView(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);

        FieldCell[][] newBoard = new FieldCell[BOARD_SIZE][BOARD_SIZE];
        for (Integer i = 0; i < BOARD_SIZE; i++)
            for (Integer j = 0; j < BOARD_SIZE; j++)
                newBoard[i][j] = new FieldCell(null, i, j);
        client.setBoard(newBoard);
    }

    @Override
    public void render() {
        FieldCell[][] boardRep = client.getBoard();
        StackPane cell;

        colorPicker = new ChoiceBox<>();
        colorPicker.getItems().addAll("RED", "BLUE", "PURPLE");
        colorPicker.setValue("RED");

        Button confirmSelections = new Button("Confirm");
        confirmSelections.setOnAction(e -> handleConfirmation((Button)e.getSource()));

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
                cell.setOnMouseClicked(e -> handleCellClick((StackPane)e.getSource()));
                cell.prefHeightProperty().bind(tileSideLength);
                cell.prefWidthProperty().bind(tileSideLength);

                fillCell(cell, boardRep[i-1][j-1]);

                board.getChildren().add(cell);
            }

        Label selectedGodPower = new Label("Your selected God Power is: ");

        VBox interfaceBox = new VBox (20, colorPickerBox, board, selectedGodPower);
        interfaceBox.setAlignment(Pos.CENTER);
        interfaceBox.setFillWidth(false);

        mainStage.getScene().setRoot(interfaceBox);

    }

    private void handleConfirmation(Button source) {
        String color = colorPicker.getValue().toString(); //TODO Add legality checks, remove button and picker from scene after selection
        List<Integer> xCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(Color.RED))
                .map(cell -> Integer.parseInt(cell.getId().substring(0, 1)))
                .collect(Collectors.toList());

        List<Integer> yCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(Color.RED))
                .map(cell -> Integer.parseInt(cell.getId().substring(2, 3)))
                .collect(Collectors.toList());

        board
            .getChildren()
            .forEach(cell ->
                    ((StackPane)cell).setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));

        MessageBox.show(xCoordinates.toString() + "\n" + yCoordinates.toString(), "Selection");

        sendToServer(new WorkerSelectionEvent(xCoordinates, yCoordinates, color));
    }

    private void fillCell(StackPane cell, FieldCell fieldCell) {
        Double baseWidth = 0.9 * cell.getPrefWidth();

        Rectangle block;

        for (Integer i = 0; i < fieldCell.getHeight(); i++) {
            block = new Rectangle(baseWidth, baseWidth);
            block.setFill(Color.web("#F0DC7E"));
            block.setStroke(Color.BLACK);

            cell.getChildren().add(block);

            baseWidth *= 0.8;
        }

        if (fieldCell.isComplete()) {
            Circle dome = new Circle(cell.getPrefWidth()/2, cell.getPrefHeight()/2, baseWidth, Color.ALICEBLUE);
            cell.getChildren().add(dome);
        }

        if (fieldCell.getWorker() != null) {
            Circle worker = new Circle(cell.getPrefWidth()/2, cell.getPrefHeight()/2, baseWidth/2, Color.valueOf(fieldCell
                                                                                                                .getWorker()
                                                                                                                .getOwner()
                                                                                                                .getColour()
                                                                                                                .toUpperCase()));
            cell.getChildren().add(worker);
        }
    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/5 - edgeTolerance,(float)mainStage.getScene().getHeight()/5 - edgeTolerance));
    }

    private void handleCellClick(StackPane clickedCell) {
        toggleCell(clickedCell);
    }

    private void toggleCell(StackPane clickedCell) {
        System.out.println(clickedCell.getBorder().getStrokes().get(0));
        if (clickedCell.getBorder().getStrokes().get(0).getLeftStroke().equals(Color.BLACK)) {
            clickedCell.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            clickedCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

}
