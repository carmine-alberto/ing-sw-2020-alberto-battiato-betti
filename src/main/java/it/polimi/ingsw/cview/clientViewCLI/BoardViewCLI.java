package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import it.polimi.ingsw.model.*;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardViewCLI extends CLIView {
    private String selectedColour;

    public BoardViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);

        client.setAvailableCellsX(new ArrayList<>());
        client.setAvailableCellsY(new ArrayList<>());
    }

    @Override
    public synchronized void render() {
        FieldCell[][] boardRep = client.getBoard();
        showBoard(boardRep);
    }

    @Override
    public void handleCLIInput(String input) {
        notify(new UserInputEvent(input));
    }
}
