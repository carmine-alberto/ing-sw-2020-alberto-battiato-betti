package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.LoginEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginViewState extends GUIViewState {
    private ChoiceBox<String> cliGUIChoice;
    private TextField usernameInput;
    private TextField serverIPInput;
    private TextField portInput;

    static String USERNAME = "testUsername";
    static String IP = "127.0.0.1";
    static String PORT = "1200";

    public LoginViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    @Override
    public void fXRender() {

        Image backgroundImage = new Image(this.getClass().getClassLoader().getResource("copertina_santorini_2016.jpg").toString());
        ImageView imageNode = new ImageView(backgroundImage);
        imageNode.fitHeightProperty().bind(mainStage.heightProperty());
        imageNode.setPreserveRatio(true);
        imageNode.setSmooth(true);

        Label username = new Label("Username: ");
        usernameInput = new TextField();
        usernameInput.setText(USERNAME);
        HBox usernameBox = new HBox(10, username, usernameInput);
        usernameBox.setAlignment(Pos.CENTER);

        Label serverIP = new Label("Server IP: ");
        serverIPInput = new TextField();
        serverIPInput.setText(IP);
        HBox serverIPBox = new HBox(18, serverIP, serverIPInput);
        serverIPBox.setAlignment(Pos.CENTER);

        Label port = new Label("Port:         ");
        portInput = new TextField();
        portInput.setText(PORT);

        HBox portBox = new HBox(18, port, portInput);
        portBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Connect");
        confirmButton.setOnAction(e -> sendDataToServer());

        cliGUIChoice = new ChoiceBox<>();
        cliGUIChoice.getItems().addAll("CLI", "GUI");
        cliGUIChoice.setValue("GUI");

        VBox userDataBox = new VBox(10, usernameBox, serverIPBox, portBox, cliGUIChoice, confirmButton);
        userDataBox.setAlignment(Pos.CENTER);
        userDataBox.setBackground(new Background(new BackgroundFill(new Color(0.47, 0.95, 0.98, 0.9), new CornerRadii(30), new Insets(55))));
        userDataBox.setMaxSize(400, 300);

        StackPane basePane = new StackPane();
        basePane.getChildren().addAll(imageNode, userDataBox);

        Scene scene = new Scene(basePane, 700, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * Client and server event handling is coupled - if we want to keep the ClientView - VirtualView approach, it can't be any different;
     * otherwise, we should split the Controller in ClientController - ServerController and decouple event handling
     */
    private void sendDataToServer() {
        if (cliGUIChoice.getValue().equals("CLI")) {
            view.setRendererChoice("CLI");
        } else
            view.setRendererChoice("");

        String username = usernameInput.getText();
        String serverIP = serverIPInput.getText();
        Integer portNumber = Integer.parseInt(portInput.getText());


        try {
            setupConnectionToServer(serverIP, portNumber);

            view.setMyName(username);

            notify(new LoginEvent(username));

        } catch (IOException ex) {
            connectionClosedHandler();
        }
    }

}



