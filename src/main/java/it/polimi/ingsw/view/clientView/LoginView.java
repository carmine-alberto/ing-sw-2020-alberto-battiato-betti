package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.controller.events.ChangeViewEvent;
import it.polimi.ingsw.controller.events.Event;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class LoginView extends View {
    private ChoiceBox<String> cliGUIChoice;
    private TextField usernameInput;
    private TextField serverIPInput;

    public LoginView(Stage stage, Socket clientSocket, View viewState) {
        super(stage, clientSocket, viewState);
    }

    @Override
    public void render() {

        Image backgroundImage = new Image("file:" + System.getProperty("user.dir") + "\\resources\\copertina_santorini_2016.jpg");
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

    private void sendDataToServer() {
        String interfaceChoice = cliGUIChoice.getValue();
        //TODO Controllo su validità di username e IP inseriti
        String username = usernameInput.getText();
        String serverIP = serverIPInput.getText();
        Integer PORT_NUMBER = 1200;

        try {
            clientSocket = new Socket(serverIP, PORT_NUMBER);
            ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

            clientOutputStream.writeObject(new LoginEvent(username));

            ChangeViewEvent serverEvent = (ChangeViewEvent) clientInputStream.readObject();

            next(serverEvent.nextState.getClientName());
        } catch (IOException | ClassNotFoundException ex) {
            connectionClosedHandler();
        }
    }
}



