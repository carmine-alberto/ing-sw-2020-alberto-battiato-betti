package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.model.FieldCell;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utility.GameSettings.*;


public class BoardViewState extends GUIViewState {
    private static final Integer EDGE_TOLERANCE = 30;
    private static final Integer PADDING_SIZE = 8;
    private static final Paint NEUTRAL_STATE_COLOR = Color.TRANSPARENT;
    private static final Paint FOCUSED_STATE_COLOR = Color.RED;

    private TilePane board;
    private Image boardBackground;
    private BackgroundImage bGImage;

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
            board.setPadding(new Insets(PADDING_SIZE));

            boardBackground  = new Image(this.getClass().getClassLoader().getResource("SantoriniBoardCut.png").toString());
            BackgroundSize backGroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
            bGImage = new BackgroundImage(boardBackground, null, null, null,  backGroundSize);
            board.setBackground(new Background(bGImage));

            FloatProperty tileSideLength = new SimpleFloatProperty(Math.min((float) mainStage.getScene().getWidth() / FIELD_SIZE - EDGE_TOLERANCE, (float) mainStage.getScene().getHeight() / FIELD_SIZE - EDGE_TOLERANCE));
            mainStage.getScene().heightProperty().addListener(e -> {updateProperty(tileSideLength); });
            mainStage.getScene().widthProperty().addListener(e -> {updateProperty(tileSideLength); });

            board.setOrientation(Orientation.HORIZONTAL);
            board.setPrefRows(FIELD_SIZE);
            board.setPrefColumns(FIELD_SIZE);
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

                    fillCell(cell, boardRep[i - OFFSET][j - OFFSET]);

                    board.getChildren().add(cell);
                }

            Label selectedGodPower = new Label(view.getMyName() + ", your selected God Power is: " + view.getPlayerInfos().get(view.getMyName()).get(FIRST_PLAYER_INDEX));

            VBox interfaceBox;
            interfaceBox = new VBox(10, board, selectedGodPower, new Circle(17.5, Color.web(view.getPlayerInfos().get(view.getMyName()).get(SECOND_ELEMENT_INDEX))));
            interfaceBox.setPadding(new Insets(EDGE_TOLERANCE));
            interfaceBox.setAlignment(Pos.CENTER);
            interfaceBox.setFillWidth(false);

            mainStage.getScene().setRoot(interfaceBox);
        }
    }

    protected void fillCell(StackPane cell, FieldCell fieldCell) {
        List<Integer> availableXCoordinates = view.getAvailableCellsX();
        List<Integer> availableYCoordinates = view.getAvailableCellsY();

        super.fillCell(cell, fieldCell);

        for (Integer i = 0; i < availableXCoordinates.size(); i++)
            if (fieldCell.getPosX().equals(availableXCoordinates.get(i))
                                        &&
                fieldCell.getPosY().equals(availableYCoordinates.get(i)))
                    toggleCell(cell);
    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/FIELD_SIZE - EDGE_TOLERANCE,(float)mainStage.getScene().getHeight()/FIELD_SIZE - EDGE_TOLERANCE));
    }

    private void handleCellClick(StackPane clickedCell) {
        notify(new UserInputEvent(clickedCell.getId()));
    }

    private void toggleCell(StackPane clickedCell) {
        if (clickedCell.getBorder().getStrokes().get(0).getLeftStroke().equals(NEUTRAL_STATE_COLOR)) {
            clickedCell.setBorder(new Border(new BorderStroke(FOCUSED_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(FIELD_SIZE))));
        } else {
            clickedCell.setBorder(new Border(new BorderStroke(NEUTRAL_STATE_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

}
