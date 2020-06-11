package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginViewStateCLI extends CLIViewState {

    public LoginViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
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
