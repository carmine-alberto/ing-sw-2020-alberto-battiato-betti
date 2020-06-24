package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GodPowerViewStateCLI extends CLIViewState {
    private enum InternalState {
        GOD_SELECTION,
        IDLE
    }
    private List<String> availableGods;
    private InternalState currentState;

    public GodPowerViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
        this.currentState = InternalState.GOD_SELECTION;
    }

    @Override
    public void render() {
        availableGods = view.getAvailableGods();

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

    @Override
    public void showWarning(String message) {
        super.showWarning(message);
        if (currentState == InternalState.IDLE) {
            currentState = InternalState.GOD_SELECTION;
            this.render();
        }
    }
}