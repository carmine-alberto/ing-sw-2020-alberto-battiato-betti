package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.model.FieldCell;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GameSettings.FIELD_SIZE;
import static it.polimi.ingsw.GameSettings.NUM_OF_WORKERS;
import static it.polimi.ingsw.view.clientView.WorkerSetupViewState.CellCoordinate.X;
import static it.polimi.ingsw.view.clientView.WorkerSetupViewState.CellCoordinate.Y;

public class WorkerSetupViewState extends GUIViewState {
    enum CellCoordinate {
        X, Y;
    }

    private static final Paint NEUTRAL_STATE_COLOR = Color.TRANSPARENT;
    private static final Paint FOCUSED_STATE_COLOR = Color.RED;
    private static final Integer EDGE_TOLERANCE = 20;

    private ComboBox<Rectangle> colors;
    private TilePane board;
    private Image boardBackground;
    private BackgroundImage bGImage;

    public WorkerSetupViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    @Override
    protected void fXRender() {
        FieldCell[][] boardRep = view.getBoard();
        StackPane cell;

        if (boardRep != null) {
            colors = new ComboBox<>();
            Rectangle cyan = new Rectangle(60 , 10);
            cyan.setFill(Color.CYAN);
            Rectangle magenta = new Rectangle(60 , 10);
            magenta.setFill(Color.MAGENTA);
            Rectangle yellow = new Rectangle(60 , 10);
            yellow.setFill(Color.YELLOW);
            colors.getItems().addAll(cyan, yellow, magenta);
            colors.getSelectionModel().select(cyan);

            Button confirmSelections = new Button("Confirm");
            confirmSelections.setOnAction(e -> handleConfirmation((Button) e.getSource()));

            HBox colorPickerBox = new HBox(10, colors, confirmSelections);

            board = new TilePane();
            board.setPadding(new Insets(8));

            boardBackground  = new Image(this.getClass().getClassLoader().getResource("SantoriniBoardCut.png").toString());
            BackgroundSize backGroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
            bGImage = new BackgroundImage(boardBackground, null, null, null,  backGroundSize);
            board.setBackground(new Background(bGImage));

            FloatProperty tileSideLength = new SimpleFloatProperty(Math.min((float) mainStage.getScene().getWidth() / 5 - EDGE_TOLERANCE, (float) mainStage.getScene().getHeight() / 5 - EDGE_TOLERANCE));
            mainStage.getScene().heightProperty().addListener(e -> updateProperty(tileSideLength));
            mainStage.getScene().widthProperty().addListener(e -> updateProperty(tileSideLength));

            board.setOrientation(Orientation.HORIZONTAL);
            board.setPrefRows(5);
            board.setPrefColumns(5);
            board.prefTileWidthProperty().bind(tileSideLength);
            board.prefTileHeightProperty().bind(tileSideLength);

            for (Integer i = 1; i <= FIELD_SIZE; i++)
                for (Integer j = 1; j <= FIELD_SIZE; j++) {
                    cell = new StackPane();
                    cell.setId(i.toString() + " " + j.toString());
                    cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    cell.setBorder(new Border(new BorderStroke(NEUTRAL_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        String color = colors.getValue().getFill().toString();

        color = switch (color) {
            case "0x00ffffff" -> "CYAN";
            case "0xff00ffff" -> "MAGENTA";
            case "0xffff00ff" -> "YELLOW";
            default -> colors.getValue().getFill().toString();
        };


        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                if(view.getBoard()[i][j].getWorker() != null && view.getBoard()[i][j].getWorker().getOwner().getColour().toUpperCase().equals(color)) {
                    this.showMessage("One of your opponents already chose this color, pick another one!");
                    return;
                }

        if (Math.toIntExact(board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane) cell).getBorder().getStrokes().get(0).getLeftStroke().equals(FOCUSED_STATE_COLOR))
                .count()) != NUM_OF_WORKERS) {
            this.showMessage("Enter the position of exactly 2 workers");
            return;
        }

        List<Integer> xCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(FOCUSED_STATE_COLOR))
                .map(cell -> extractCellCoordinate(X, cell))
                .collect(Collectors.toList());

        List<Integer> yCoordinates = board
                .getChildren()
                .stream()
                .filter(cell -> ((StackPane)cell).getBorder().getStrokes().get(0).getLeftStroke().equals(FOCUSED_STATE_COLOR))
                .map(cell -> extractCellCoordinate(Y, cell))
                .collect(Collectors.toList());

        if(!view.getBoard()[xCoordinates.get(0) - 1][yCoordinates.get(0) - 1].isFree() || !view.getBoard()[xCoordinates.get(1) - 1][yCoordinates.get(1) - 1].isFree()) {
            this.showMessage("You can't place a worker in a cell that is already occupied");
            return;
        }

        board
        .getChildren()
        .forEach(cell ->
                ((StackPane)cell).setBorder(new Border(new BorderStroke(NEUTRAL_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));


        notify(new WorkerSelectionEvent(xCoordinates, yCoordinates, color));
    }

    private Integer extractCellCoordinate(WorkerSetupViewState.CellCoordinate coord, Node cell) {
        return switch (coord) {
            case X -> Integer.parseInt(cell.getId().substring(0, 1));
            case Y -> Integer.parseInt(cell.getId().substring(2, 3));
        };
    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/5 - EDGE_TOLERANCE,(float)mainStage.getScene().getHeight()/5 - EDGE_TOLERANCE));
    }

    private void handleCellClick(StackPane clickedCell) {
        toggleCell(clickedCell);
    }

    private void toggleCell(StackPane clickedCell) {
        if (clickedCell.getBorder().getStrokes().get(0).getLeftStroke().equals(NEUTRAL_STATE_COLOR)) {
            clickedCell.setBorder(new Border(new BorderStroke(FOCUSED_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            clickedCell.setBorder(new Border(new BorderStroke(NEUTRAL_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }
}

