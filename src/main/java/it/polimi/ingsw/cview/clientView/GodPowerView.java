package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.View;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GodPowerView extends View {
    private FlowPane godsIcons;

    private List<String> godsList = List.of("Empty");

    public GodPowerView(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    public void render() {
        Label selectGodLabel = new Label("Select your desired God Power: ");

        godsIcons = new FlowPane();
        godsIcons.setAlignment(Pos.CENTER);
        godsIcons.setHgap(8);
        godsIcons.setVgap(10);
        for (String god : godsList) {
            Button godButton = new Button(god);
            godButton.setOnAction(e -> sendSelectionToServer((Button) e.getSource()));
            godsIcons.getChildren().add(godButton);

            VBox selectGodBox = new VBox(30, selectGodLabel, godsIcons);
            selectGodBox.setAlignment(Pos.CENTER);

            mainStage.getScene().setRoot(selectGodBox);

        }
    }

    public void setGodsList(List <String> godsList) {
        this.godsList = godsList;
    }


    private void sendSelectionToServer(Button pressedButton) {
        sendToServer(new GodSelectionEvent(pressedButton.getText()));
    }
}
