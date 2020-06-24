package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.controller.events.LoginEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginViewStateCLI extends CLIViewState {
    private static final Integer MIN_PORT_NUM = 0;
    private static final Integer MAX_PORT_NUM = 65535;

    /**
     * Solution viable because of the little number of microstates.
     * The State pattern has been applied to the macroCLIStates,
     * a simpler old-school enum state machine to the microCLIStates
     */

    private enum InternalState {
        IP_SELECTION,
        PORT_SELECTION,
        USERNAME_SELECTION,
        IDLE
    }

    private Integer selectedPort;
    private String selectedIP;
    private String selectedUsername;
    private InternalState currentState;


    public LoginViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
        currentState = InternalState.IP_SELECTION;
        selectedIP = "127.0.0.1";
        selectedPort = 1200;
    }


    @Override
    public synchronized void render() {
            switch (currentState) {
                case IP_SELECTION -> showMessage(getIPSelectionMessage());
                case PORT_SELECTION -> showMessage(getPortSelectionMessage());
                case USERNAME_SELECTION-> showMessage(getUsernameSelectionMessage());
            }
    }


    @Override
    public void handleCLIInput(String input) {
        switch (currentState) {
            case IP_SELECTION -> handleIPSelection(input);
            case PORT_SELECTION -> handlePortSelection(input);
            case USERNAME_SELECTION-> handleUsernameSelection(input);
        }
    }

    private void handleUsernameSelection(String input) {
        view.setMyName(input);
        currentState = InternalState.IDLE;

        notify(new LoginEvent(input));
    }

    private void handlePortSelection(String input) {
        try {
            if (!input.equals("")) {
                Integer selectedPort = Integer.parseInt(input);
                if (selectedPort < MIN_PORT_NUM || selectedPort > MAX_PORT_NUM)
                    throw new NumberFormatException();
                this.selectedPort = selectedPort;
            }

            setupConnectionToServer(selectedIP, selectedPort);

            currentState = InternalState.USERNAME_SELECTION;

        } catch (NumberFormatException exception) {
            showWarning("Invalid input: not a number or out of bounds!");
        } catch (IOException e) {
            showWarning("Server busy or unreachable - try again");
            currentState = InternalState.IP_SELECTION;
        }

    }

    private void handleIPSelection(String input) {
        String octaveIP = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

        if (input.equals(""))
            currentState = InternalState.PORT_SELECTION;
        else if (input.matches(octaveIP + "\\." + octaveIP + "\\." + octaveIP + "\\." + octaveIP)) {
            selectedIP = input;
            currentState = InternalState.PORT_SELECTION;
        } else
            showWarning("Invalid IP address - Try again");

    }

    private String getUsernameSelectionMessage() {
        return "Insert Username: ";
    }

    private String getIPSelectionMessage() {
        return "Insert Server IP (blank for localhost):  ";
    }

    private String getPortSelectionMessage() {
        return "Insert port number (blank for 1200): ";
    }

    @Override
    public void showWarning(String message) {
        super.showWarning(message);
        if (currentState == InternalState.IDLE) {
            currentState = InternalState.USERNAME_SELECTION;
            this.render();
        }
    }
}
