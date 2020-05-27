package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.View;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GodPowerView extends GUIView {
    private FlowPane godsIcons;

    public GodPowerView(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    public void fXRender() {
        Label selectGodLabel = new Label("Select your desired God Power: ");
        List<String> godsList = client.getAvailableGods();

        godsIcons = new FlowPane();
        godsIcons.setAlignment(Pos.CENTER);
        godsIcons.setHgap(8);
        godsIcons.setVgap(10);
        if (godsList != null)
            for (String god : godsList) {
                Button godButton = new Button(god);
                godButton.setOnAction(e -> sendSelectionToServer((Button) e.getSource()));
                godsIcons.getChildren().add(godButton);
            }
        VBox selectGodBox = new VBox(30, selectGodLabel, godsIcons);
        selectGodBox.setAlignment(Pos.CENTER);

        mainStage.getScene().setRoot(selectGodBox);
    }


    private void sendSelectionToServer(Button pressedButton) {
        notify(new GodSelectionEvent(pressedButton.getText()));
    }
}
