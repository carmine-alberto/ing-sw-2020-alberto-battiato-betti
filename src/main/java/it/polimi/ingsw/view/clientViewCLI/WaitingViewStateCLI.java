package it.polimi.ingsw.view.clientViewCLI;

import it.polimi.ingsw.view.View;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class WaitingViewStateCLI extends CLIViewState {

    public WaitingViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }


    @Override
    public void render() {
        showMessage("Waiting for the other players...");
    }

    @Override
    public void handleCLIInput(String input) {
        showWarning("Be patient man");
    }
}
