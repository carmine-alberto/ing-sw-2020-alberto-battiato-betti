package it.polimi.ingsw.cview.clientView;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.LoginEvent;
import it.polimi.ingsw.cview.View;
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

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginView extends View {
    private ChoiceBox<String> cliGUIChoice;
    private TextField usernameInput;
    private TextField serverIPInput;

    public LoginView(Stage stage, Socket clientSocket, Client client) {
        super(stage, clientSocket, client, null);
    }

    @Override
    public void render() {

        Image backgroundImage = new Image("file:" + System.getProperty("user.dir") + File.separator + "resources" + File.separator + "copertina_santorini_2016.jpg");
        ImageView imageNode = new ImageView(backgroundImage);
        imageNode.fitHeightProperty().bind(mainStage.heightProperty());
        imageNode.setPreserveRatio(true);
        imageNode.setSmooth(true);

        Label username = new Label("Username: ");
        usernameInput = new TextField();
        usernameInput.setText("testUsername"); //TODO Remove hardcoded value
        HBox usernameBox = new HBox(10, username, usernameInput);
        usernameBox.setAlignment(Pos.CENTER);

        Label serverIP = new Label("Server IP: ");
        serverIPInput = new TextField();
        serverIPInput.setText("127.0.0.1"); //TODO Remove hardcoded value
        HBox serverIPBox = new HBox(18, serverIP, serverIPInput);
        serverIPBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Connect");
        confirmButton.setOnAction(e -> sendDataToServer());

        cliGUIChoice = new ChoiceBox<>();
        cliGUIChoice.getItems().addAll("CLI", "GUI");
        cliGUIChoice.setValue("GUI");

        VBox userDataBox = new VBox(10, usernameBox, serverIPBox, cliGUIChoice, confirmButton);
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
        if (cliGUIChoice.getValue().equals("CLI"))
            client.setRendererChoice("CLI");
        else
            client.setRendererChoice("");
        //TODO Add Port textBox
        String username = usernameInput.getText();
        String serverIP = serverIPInput.getText();
        Integer PORT_NUMBER = 1200;

        try {
            clientSocket = new Socket(serverIP, PORT_NUMBER);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

            Thread serverListener = new Thread(new ViewEventHandler(client, clientInputStream));
            serverListener.start();
            System.out.println("Thread started");

            client.setMyName(username);

            out.writeObject(new LoginEvent(username));

        } catch (IOException ex) {
            connectionClosedHandler();
        }
    }

}



