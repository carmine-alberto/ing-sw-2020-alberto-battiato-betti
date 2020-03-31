package it.polimi.ingsw.view.clientView;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GodPowerView extends View {
    private FlowPane godsIcons;

    public GodPowerView(Stage stage, Socket clientSocket, View viewState) {
        super(stage, clientSocket, viewState);
    }

    @Override
    public void render() {
        List<String> godsList = new ArrayList<>(List.of("Apollo", "Athena", "Artemis")); //TODO The event triggering the state change should pass available god powers

        Label selectGodLabel = new Label("Select your desired God Power: ");

        godsIcons = new FlowPane();
        godsIcons.setAlignment(Pos.CENTER);
        godsIcons.setHgap(8);
        godsIcons.setVgap(10);
        for (String god: godsList)
            godsIcons.getChildren().add(new Button(god));

        VBox selectGodBox = new VBox(30, selectGodLabel, godsIcons);
        selectGodBox.setAlignment(Pos.CENTER);

        mainStage.getScene().setRoot(selectGodBox);

    }
}
