package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.view.utility.MessageBox;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.Socket;

public class MainView extends View {
    FieldCell[][] board = new FieldCell[5][5];
    final Integer edgeTolerance = 20;

    public MainView(Stage stage, Socket clientSocket, View viewState) {
        super(stage, clientSocket, viewState);
    }

    @Override
    public void render() {
        Button cell;

        TilePane board = new TilePane();
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
                cell = new Button();
                cell.setId(i.toString() + " " + j.toString());
                cell.setBackground(new Background(new BackgroundFill(new Color(0.47, 0.95, 0.98, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
                cell.setStyle("-fx-border-color: black");
                cell.setOnAction(e -> handleCellClick((Button)e.getSource()));
                cell.prefHeightProperty().bind(tileSideLength);
                cell.prefWidthProperty().bind(tileSideLength);
                board.getChildren().add(cell);
            }

        Label selectedGodPower = new Label("Your selected God Power is: ");

        VBox interfaceBox = new VBox (20, board, selectedGodPower);
        interfaceBox.setAlignment(Pos.CENTER);
        interfaceBox.setFillWidth(false);

        mainStage.getScene().setRoot(interfaceBox);

    }

    private void updateProperty(FloatProperty tileSideLength) {
        tileSideLength.set(Math.min((float)mainStage.getScene().getWidth()/5 - edgeTolerance,(float)mainStage.getScene().getHeight()/5 - edgeTolerance));
    }

    private void handleCellClick(Button clickedCell) {
        MessageBox.show(clickedCell.getId(), "Cell clicked");
    }

 }
