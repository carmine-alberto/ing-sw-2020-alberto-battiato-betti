package it.polimi.ingsw.view;

import it.polimi.ingsw.view.utility.MessageBox;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ChallengerSelectionView extends View {
    private ToggleGroup numberOfPlayers;
    private FlowPane godsIcons;


    public ChallengerSelectionView(Stage stage, Socket clientSocket, View viewState) {
        super(stage, clientSocket, viewState);
    }


    @Override
    public void render() {
        numberOfPlayers = new ToggleGroup();

        Label numberOfPlayersLabel = new Label("Select the number of players: ");

        RadioButton twoPlayers = new RadioButton("2 Players");
        twoPlayers.setToggleGroup(numberOfPlayers);

        RadioButton threePlayers = new RadioButton(("3 Players"));
        threePlayers.setToggleGroup(numberOfPlayers);
        numberOfPlayers.selectToggle(twoPlayers);


        HBox numberOfPlayersBox = new HBox(5, numberOfPlayersLabel, twoPlayers, threePlayers);
        numberOfPlayersBox.setAlignment(Pos.CENTER);

        Label godPowersLabel = new Label("Select the God Powers to be used: ");

        List<String> godsList = new ArrayList<>(List.of("Apollo", "Athena", "Artemis","Atlas", "Apollo", "Athena", "Artemis","Atlas", "Apollo", "Athena", "Artemis","Atlas")); //TODO Fill the list the correct way

        godsIcons = new FlowPane();
        godsIcons.setHgap(8);
        godsIcons.setVgap(10);
        for (String god: godsList)
            godsIcons.getChildren().add(new ToggleButton(god));


        HBox godPowersBox = new HBox(10, godPowersLabel, godsIcons);
        godPowersBox.setAlignment(Pos.CENTER);

        Button sendSelectionButton = new Button("Continue");
        sendSelectionButton.setOnAction(e -> sendDataToServer());

        VBox userSelectionsBox = new VBox(50, numberOfPlayersBox, godPowersBox, sendSelectionButton);
        userSelectionsBox.setAlignment(Pos.CENTER);
        userSelectionsBox.setPadding(new Insets(20));

        Scene scene = new Scene(userSelectionsBox);

        mainStage.setScene(scene);
        mainStage.show();

    }

    private void sendDataToServer() {
        List<String> selectedGods = godsIcons.getChildren().stream()
                            .filter(godButton -> ((ToggleButton)godButton).isSelected())
                            .map(godButton -> ((ToggleButton) godButton).getText())
                            .collect(Collectors.toList());

        Integer selectedNumberOfPlayers = Integer.parseInt(
                                        ((RadioButton)numberOfPlayers.getSelectedToggle())
                                        .getText()
                                        .substring(0, 1)
        );

        if (selectedNumberOfPlayers != selectedGods.size())
            MessageBox.show("Number of players and god powers must match!", "Error");
        else {
            try {
                new ObjectOutputStream(clientSocket.getOutputStream()).writeObject(new ChallengerSelectionEvent(selectedNumberOfPlayers, selectedGods));
            } catch (IOException e) {
                connectionClosedHandler();
            }
        }
    }


    @Override
    public void next() {

    }
}
