package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GodPowerViewCLI extends CLIView {
    private enum InternalState {
        GOD_SELECTION,
        IDLE
    }
    private List<String> availableGods;
    private InternalState currentState;

    public GodPowerViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
        this.currentState = InternalState.GOD_SELECTION;
    }

    @Override
    public void render() {
        availableGods = client.getAvailableGods();

        if (availableGods != null)
            switch (currentState) {
                case GOD_SELECTION -> showMessage(getDesiredGodMessage());
            }
    }

    @Override
    public void handleCLIInput(String input) {
        switch (currentState) {
            case GOD_SELECTION -> handleGodSelection(input);
        }
    }

    private void handleGodSelection(String input) {
        String sanitizedInput = CLIFormatter.capitalize(input);
        if (availableGods.contains(sanitizedInput)) {
            currentState = InternalState.IDLE;
            notify(new GodSelectionEvent(sanitizedInput));
        } else
            showWarning("The chosen God is not available!");
    }

    private String getDesiredGodMessage() {
        return String.format("""
                    God Powers available: %s

                    Enter the name of the desired God of yours: """,
                CLIFormatter.formatStringList(availableGods)
        );
    }

}