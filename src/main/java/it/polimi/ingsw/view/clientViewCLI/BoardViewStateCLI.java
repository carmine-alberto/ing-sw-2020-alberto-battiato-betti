package it.polimi.ingsw.view.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.model.*;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BoardViewStateCLI extends CLIViewState {
    private String selectedColour;

    public BoardViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);

        view.setAvailableCellsX(new ArrayList<>());
        view.setAvailableCellsY(new ArrayList<>());
    }

    @Override
    public synchronized void render() {
        FieldCell[][] boardRep = view.getBoard();
        showBoard(boardRep);
    }

    @Override
    public void handleCLIInput(String input) {
        notify(new UserInputEvent(input));
    }

    @Override
    public synchronized void showWarning(String message) {
        super.showWarning(message);
    }

    @Override
    public synchronized void showMessage(String message) {
        super.showMessage(message);
    }
}
