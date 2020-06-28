package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GodPowerViewState extends GUIViewState {
    private FlowPane godsIcons;

    static final Integer H_GAP = 8;
    static final Integer V_GAP = 10;
    static final Integer V_BOX = 30;

    public GodPowerViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    @Override
    public void fXRender() {
        Label selectGodLabel = new Label("Select your desired God Power: ");
        List<String> godsList = view.getAvailableGods();

        godsIcons = new FlowPane();
        godsIcons.setAlignment(Pos.CENTER);
        godsIcons.setHgap(H_GAP);
        godsIcons.setVgap(V_GAP);

        if (godsList != null)
            for (String god : godsList) {
                Button godButton = new Button(god);
                godButton.setOnAction(e -> sendSelectionToServer((Button) e.getSource()));
                godsIcons.getChildren().add(godButton);
            }

        VBox selectGodBox = new VBox(V_BOX, selectGodLabel, godsIcons);
        selectGodBox.setAlignment(Pos.CENTER);

        mainStage.getScene().setRoot(selectGodBox);
    }

    private void sendSelectionToServer(Button pressedButton) {
        notify(new GodSelectionEvent(pressedButton.getText()));
    }
}
