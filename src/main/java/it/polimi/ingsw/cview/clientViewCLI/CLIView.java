package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import it.polimi.ingsw.cview.utility.MessageWindow;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public abstract class CLIView extends View {

    public CLIView(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    protected void connectionClosedHandler() {
        CLIFormatter.print("Connection closed - Restart the client and try again!");
        System.exit(1);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showWarning(String message) {
        System.out.println(CLIFormatter.ANSI_RED + message + CLIFormatter.ANSI_RESET);
    }

    @Override
    public void showChoices(List<String> availableChoices) {
        System.out.println(CLIFormatter.formatStringList(availableChoices));
    }

    public abstract void handleCLIInput(String input);
}
