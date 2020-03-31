package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.utility.MessageBox;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private ToggleGroup startingPlayer;
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


        startingPlayer = new ToggleGroup();

        Label startingPlayerLabel = new Label("Select the starting player: ");

        RadioButton firstToConnect = new RadioButton("1");
        firstToConnect.setToggleGroup(startingPlayer);

        RadioButton secondToConnect = new RadioButton(("2"));
        secondToConnect.setToggleGroup(startingPlayer);

        RadioButton thirdToConnect = new RadioButton("3");
        thirdToConnect.setToggleGroup(startingPlayer);

        startingPlayer.selectToggle(firstToConnect);

        HBox startingPlayerBox = new HBox(5, startingPlayerLabel, firstToConnect, secondToConnect, thirdToConnect);
        startingPlayerBox.setAlignment(Pos.CENTER);


        Label godPowersLabel = new Label("Select the God Powers to be used: ");

        List<String> godsList = new ArrayList<>(List.of("Apollo", "Athena", "Artemis", "Atlas", "Apollo", "Athena", "Artemis", "Atlas", "Apollo", "Athena", "Artemis", "Atlas")); //TODO Fill the list the correct way

        godsIcons = new FlowPane();
        godsIcons.setHgap(8);
        godsIcons.setVgap(10);
        for (String god : godsList)
            godsIcons.getChildren().add(new ToggleButton(god));


        HBox godPowersBox = new HBox(10, godPowersLabel, godsIcons);
        godPowersBox.setAlignment(Pos.CENTER);

        Button sendSelectionButton = new Button("Continue");
        sendSelectionButton.setOnAction(e -> sendDataToServer());

        VBox userSelectionsBox = new VBox(50, numberOfPlayersBox, startingPlayerBox, godPowersBox, sendSelectionButton);
        userSelectionsBox.setAlignment(Pos.CENTER);
        userSelectionsBox.setPadding(new Insets(20));

        mainStage.getScene().rootProperty().set(userSelectionsBox);

    }

    private void sendDataToServer() {
        List<String> selectedGods = godsIcons.getChildren().stream()
                .filter(godButton -> ((ToggleButton) godButton).isSelected())
                .map(godButton -> ((ToggleButton) godButton).getText())
                .collect(Collectors.toList());

        Integer selectedNumberOfPlayers = Integer.parseInt(
                ((RadioButton) numberOfPlayers.getSelectedToggle())
                        .getText()
                        .substring(0, 1)
        );

        Integer selectedStartingPlayer = Integer.parseInt(
                ((RadioButton) startingPlayer.getSelectedToggle())
                        .getText()
        );

        if (selectedNumberOfPlayers != selectedGods.size())
            MessageBox.show("Number of players and god powers must match!", "Error");
        else if (selectedStartingPlayer > selectedNumberOfPlayers)
            MessageBox.show("You can't choose the third player as starting player in a 2-players game!", "Error");
        else {
            try {
                //TODO Handle ChangeViewEvent
                new ObjectOutputStream(clientSocket.getOutputStream()).writeObject(new ChallengerSelectionEvent(selectedNumberOfPlayers, selectedGods, selectedStartingPlayer));
            } catch (IOException e) {
                connectionClosedHandler();
            }
        }
    }
}
