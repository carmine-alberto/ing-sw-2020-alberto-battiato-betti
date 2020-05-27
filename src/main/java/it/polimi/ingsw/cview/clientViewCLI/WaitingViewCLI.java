package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.cview.View;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class WaitingViewCLI extends CLIView {

    public WaitingViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
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
