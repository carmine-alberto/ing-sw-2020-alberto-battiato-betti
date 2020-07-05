package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.viewUtility.MessageWindow;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;


public class ChallengerSelectionViewState extends GUIViewState {
    private ToggleGroup numberOfPlayers;
    private ToggleGroup startingPlayer;
    private FlowPane godsIcons;

    static final Integer HBOX_LENGHT = 5;
    static final Integer H_GAP = 8;
    static final Integer V_GAP = 10;
    static final Integer HBOX_GODS = 10;
    static final Integer PAD = 20;
    static final Integer HBOX_USERS = 50;

    public ChallengerSelectionViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    @Override
    protected void fXRender() {
        numberOfPlayers = new ToggleGroup();

        Label numberOfPlayersLabel = new Label("Select the number of players: ");

        RadioButton twoPlayers = new RadioButton("2 Players");
        twoPlayers.setToggleGroup(numberOfPlayers);

        RadioButton threePlayers = new RadioButton(("3 Players"));
        threePlayers.setToggleGroup(numberOfPlayers);
        numberOfPlayers.selectToggle(twoPlayers);

        HBox numberOfPlayersBox = new HBox(HBOX_LENGHT, numberOfPlayersLabel, twoPlayers, threePlayers);
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

        HBox startingPlayerBox = new HBox(HBOX_LENGHT, startingPlayerLabel, firstToConnect, secondToConnect, thirdToConnect);
        startingPlayerBox.setAlignment(Pos.CENTER);


        Label godPowersLabel = new Label("Select the God Powers to be used: ");

        godsIcons = new FlowPane();
        godsIcons.setHgap(H_GAP);
        godsIcons.setVgap(V_GAP);

        List<String> godsList = view.getAvailableGods();
        if (godsList != null)
            for (String god : godsList){
                Image godImage = new Image(this.getClass().getClassLoader().getResource(god + ".png").toString() );
                ImageView imageView = new ImageView(godImage);
                imageView.setFitHeight(280);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                ToggleButton godButton = new ToggleButton("", imageView);
                godButton.setId(god);

                godsIcons.getChildren().add(godButton);
            }

        ScrollPane scrollPane = new ScrollPane(godsIcons);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        HBox godPowersBox = new HBox(HBOX_GODS, godPowersLabel, scrollPane);
        godPowersBox.setAlignment(Pos.CENTER);

        Button sendSelectionButton = new Button("Continue");
        sendSelectionButton.setOnAction(e -> sendDataToServer());

        VBox userSelectionsBox = new VBox(HBOX_USERS, numberOfPlayersBox, startingPlayerBox, godPowersBox, sendSelectionButton);
        userSelectionsBox.setAlignment(Pos.CENTER);
        userSelectionsBox.setPadding(new Insets(PAD));

        mainStage.getScene().rootProperty().set(userSelectionsBox);

    }

    private void sendDataToServer() {
        List<String> selectedGods = godsIcons.getChildren()
                .stream()
                .filter(godCheckBox -> ((ToggleButton) godCheckBox).isSelected())
                .map(godCheckBox -> ((ToggleButton) godCheckBox).getId())
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
            MessageWindow.show("Number of players and god powers must match!", "Error");
        else if (selectedStartingPlayer > selectedNumberOfPlayers)
            MessageWindow.show("You can't choose the third player as starting player in a 2-players game!", "Error");
        else
            notify(new ChallengerSelectionEvent(selectedNumberOfPlayers, selectedGods, selectedStartingPlayer));
    }
}
