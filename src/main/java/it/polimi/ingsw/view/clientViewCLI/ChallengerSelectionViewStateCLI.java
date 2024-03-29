package it.polimi.ingsw.view.clientViewCLI;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.view.viewUtility.CLIFormatter;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChallengerSelectionViewStateCLI extends CLIViewState {
    /**
     * Solution viable because of the little number of microstates.
     * The State pattern has been applied to the macroCLIStates,
     * a simpler old-school enum state machine to the microCLIStates
     */
    private enum InternalState {
        NUM_OF_PLAYERS_SELECTION,
        GOD_SELECTION,
        STARTER_SELECTION,
        IDLE
    }


    private Integer selectedNumber;
    private List<String> availableGods;
    private List<String> selectedGods;
    private InternalState currentState;


    public ChallengerSelectionViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
        currentState = InternalState.NUM_OF_PLAYERS_SELECTION;
        selectedGods = new ArrayList<>();
    }



    @Override
    public synchronized void render() {
        availableGods = view.getAvailableGods();

        if (availableGods != null)
            switch (currentState) {
                case NUM_OF_PLAYERS_SELECTION -> showMessage(getNumOfPlayersMessage());
                case STARTER_SELECTION -> showMessage(getStarterSelectionMessage());
                case GOD_SELECTION -> showMessage(getGodSelectionMessage());
            }
    }


    @Override
    public void handleCLIInput(String input) {
        switch (currentState) {
            case NUM_OF_PLAYERS_SELECTION -> handleNumberOfPlayers(input);
            case GOD_SELECTION -> handleGodSelection(input);
            case STARTER_SELECTION -> handleStarterSelection(input);
        }
    }

    private void handleStarterSelection(String input) {
        try {
            Integer selectedStarter = Integer.parseInt(input);
            if (selectedStarter < 0 || selectedStarter > selectedNumber)
                throw new NumberFormatException();

            currentState = InternalState.IDLE;
            notify(new ChallengerSelectionEvent(selectedNumber, selectedGods, selectedStarter));
        } catch (NumberFormatException exception) {
            showWarning("Invalid input: not a number or out of bounds!");
        }
    }

    private void handleGodSelection(String input) {
        String sanitizedInput = CLIFormatter.capitalize(input);

        if (availableGods.contains(sanitizedInput)) {
            selectedGods.add(sanitizedInput);
            availableGods.remove(sanitizedInput);

            if (selectedGods.size() == selectedNumber)
                currentState = InternalState.STARTER_SELECTION;
        } else {
            showWarning("The chosen God is not available!");
        }
    }

    private void handleNumberOfPlayers(String input) {
        if (input.equals("2") || input.equals("3")) {
            selectedNumber = Integer.parseInt(input);
            currentState = InternalState.GOD_SELECTION;
        } else
            showWarning("Invalid input!");
    }

    private String getGodSelectionMessage() {
        return String.format("""
                Which of these Gods do you want to play with?

                Available: %s

                %s left to choose: """
                , CLIFormatter.formatStringList(availableGods), (selectedNumber - selectedGods.size()));
    }

    private String getStarterSelectionMessage() {
        return String.format("""
                Select the starting player (1%s"""
                , selectedNumber == 2 ? " or 2)" : ", 2 or 3)");
    }

    private String getNumOfPlayersMessage() {
        return "Enter the number of the players (2 or 3): ";
    }

    @Override
    public void showWarning(String message) {
        super.showWarning(message);
        if (currentState == InternalState.IDLE) {
            currentState =  InternalState.NUM_OF_PLAYERS_SELECTION;
            this.render();
        }
    }
}