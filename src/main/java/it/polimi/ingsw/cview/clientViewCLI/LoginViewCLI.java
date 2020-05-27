package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.cview.View;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginViewCLI extends CLIView {

    public LoginViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    public void handleCLIInput(String input) {

    }

    @Override
    public void render() { //questa fase viene per forza fatta nella GUI

        System.out.println("Insert Username:");


        System.out.println("Insert Server IP:");


        System.out.println(" ");

    }
}
