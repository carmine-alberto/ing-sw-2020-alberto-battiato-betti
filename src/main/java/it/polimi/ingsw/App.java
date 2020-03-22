package it.polimi.ingsw;

import it.polimi.ingsw.utilityView.MessageBox;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App extends Application {
    ChoiceBox<String> cliGUIChoice;
    TextField usernameInput;
    TextField serverIPInput;
    Stage mainStage;

    //Network objects
    Socket clientSocket;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        StackPane basePane = new StackPane();
        Image backgroundImage = new Image("file:C:\\Users\\Casa\\Desktop\\ing-sw-2020-alberto-battiato-betti\\resources\\copertina_santorini_2016.jpg");
        ImageView imageNode = new ImageView(backgroundImage);

        Label username = new Label("Username: ");
        usernameInput = new TextField();
        HBox usernameBox = new HBox(10, username, usernameInput);
        usernameBox.setAlignment(Pos.CENTER);

        Label serverIP = new Label("Server IP: ");
        serverIPInput = new TextField();
        HBox serverIPBox = new HBox(18, serverIP, serverIPInput);
        serverIPBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Connect");
        confirmButton.setOnAction(e -> sendDataToServer());

        cliGUIChoice = new ChoiceBox<>();
        cliGUIChoice.getItems().addAll("CLI", "GUI");
        cliGUIChoice.setValue("GUI");

        VBox userDataBox = new VBox(10, usernameBox, serverIPBox, cliGUIChoice, confirmButton);
        userDataBox.setAlignment(Pos.CENTER);
        userDataBox.setStyle("-fx-background-color: #1cf7ff");

        basePane.getChildren().addAll(imageNode, userDataBox);

        Scene scene = new Scene(basePane, 800, 600);
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
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
            clientOutputStream.writeObject(username);
        }
        catch (IOException ex) {
            MessageBox.show("Connection closed - Restart the client and try again!", "Connection Error");
            mainStage.close();
        }

    }

    public static void main(String[] args) {
        System.out.println( "Jacopo Gay lol" );
        launch();
    }


}
