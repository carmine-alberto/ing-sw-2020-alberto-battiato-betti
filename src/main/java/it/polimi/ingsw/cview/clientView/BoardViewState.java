package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.model.FieldCell;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class BoardViewState extends GUIViewState {
    final Integer edgeTolerance = 20;
    final Integer BOARD_SIZE = 5;

    private TilePane board;

    public BoardViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);

        view.setAvailableCellsX(new ArrayList<>());
        view.setAvailableCellsY(new ArrayList<>());
    }

    @Override
    protected void fXRender() {
        if (view.getPlayerInfos() != null) {
            FieldCell[][] boardRep = view.getBoard();
            StackPane cell;

            board = new TilePane();
            FloatProperty tileSideLength = new SimpleFloatProperty(Math.min((float) mainStage.getScene().getWidth() / 5 - edgeTolerance, (float) mainStage.getScene().getHeight() / 5 - edgeTolerance));
            mainStage.getScene().heightProperty().addListener(e -> {updateProperty(tileSideLength); this.render();});
            mainStage.getScene().widthProperty().addListener(e -> {updateProperty(tileSideLength); this.render();});

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

            Label selectedGodPower = new Label(view.getMyName() + ", your selected God Power is: " + view.getPlayerInfos().get(view.getMyName()).get(0));

            VBox interfaceBox;
            interfaceBox = new VBox(20, board, selectedGodPower, new Circle(17.5, Color.web(view.getPlayerInfos().get(view.getMyName()).get(1))));
            interfaceBox.setPadding(new Insets(20));
            interfaceBox.setAlignment(Pos.CENTER);
            interfaceBox.setFillWidth(false);

            mainStage.getScene().setRoot(interfaceBox);
        }
    }


    private void fillCell(StackPane cell, FieldCell fieldCell) {
        List<Integer> availableXCoordinates = view.getAvailableCellsX();
        List<Integer> availableYCoordinates = view.getAvailableCellsY();

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

        for (Integer i = 0; i < availableXCoordinates.size(); i++)
            if (fieldCell.getPosX().equals(availableXCoordinates.get(i))
                                        &&
                fieldCell.getPosY().equals(availableYCoordinates.get(i)))
                toggleCell(cell);
    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/5 - edgeTolerance,(float)mainStage.getScene().getHeight()/5 - edgeTolerance));
    }

    private void handleCellClick(StackPane clickedCell) {
        notify(new UserInputEvent(clickedCell.getId()));
    }

    private void toggleCell(StackPane clickedCell) {
        if (clickedCell.getBorder().getStrokes().get(0).getLeftStroke().equals(Color.BLACK)) {
            clickedCell.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        } else {
            clickedCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }
}
