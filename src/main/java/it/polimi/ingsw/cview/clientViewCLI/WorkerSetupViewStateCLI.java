package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.model.FieldCell;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WorkerSetupViewStateCLI extends CLIViewState {
    private enum InternalState {
        COLOR_SELECTION,
        WORKER_SELECTION,
        IDLE
    }
    private String selectedColour;
    private InternalState currentState;
    private List<Integer> xCoordinates;
    private List<Integer> yCoordinates;

    public WorkerSetupViewStateCLI(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
        currentState = InternalState.COLOR_SELECTION;
        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();

    }

    @Override
    public void render() {
        FieldCell[][] boardRep = view.getBoard();

        if (boardRep != null)
            switch (currentState) {
                case COLOR_SELECTION -> showMessage(getSelectColourMessage());
                case WORKER_SELECTION -> {
                    showBoard(boardRep);
                    showMessage(getSelectWorkerMessage());
                }
                case IDLE -> showBoard(boardRep);
            }
    }

    @Override
    public void handleCLIInput(String input) {
        switch (currentState) {
            case COLOR_SELECTION -> handleColorSelection(input);
            case WORKER_SELECTION -> handleWorkerSelection(input);
        }
    }

    private String getSelectWorkerMessage() {
        return String.format("""
                            Select the position of the worker n°%s (format: x y): """,
                            xCoordinates.size() + 1
        );

    }

    private String getSelectColourMessage() {
        return "Choose a color for your workers (available: cyan, magenta, yellow): ";
    }

    private void handleColorSelection(String input) {
        if (!List.of("CYAN", "MAGENTA", "YELLOW").contains(input.toUpperCase())){
            showWarning("The selected color is not available, try again");
            return;
        }

        for (Integer i = 0; i < BOARD_SIZE; i++)
            for (Integer j = 0; j < BOARD_SIZE; j++)
                if (view.getBoard()[i][j].getWorker() != null && view.getBoard()[i][j].getWorker().getOwner().getColour().toUpperCase().equals(input.toUpperCase())) {
                    showWarning("One of your opponents already chose this color, pick another one!");
                    return;
                }

        selectedColour = input;
        currentState = InternalState.WORKER_SELECTION;
    }

    private void handleWorkerSelection(String input) {
        Integer tempX;
        Integer tempY;

        if (input.length() == 3) {
            try {
                tempX = Integer.parseInt(input.substring(0, 1));
                tempY = Integer.parseInt(input.substring(2, 3));

                if (isOutOfBounds(tempX) || isOutOfBounds(tempY) || isOccupied(tempX, tempY)) {
                    showWarning("The provided coordinates point to a non-available cell!");
                    return;
                }
            } catch (NumberFormatException exception) {
                showWarning("Your input contains unexpected characters!");
                return;
            }
        } else {
            showWarning("Bad format - Remember, the correct one is: \"x y\", where x is the row index and y is the column index");
            return;
        }
        xCoordinates.add(tempX);
        yCoordinates.add(tempY);

        if (xCoordinates.size() == 2) {
            currentState = InternalState.IDLE;
            notify(new WorkerSelectionEvent(xCoordinates, yCoordinates, selectedColour));
        }
    }

    private Boolean isOccupied(Integer tempX, Integer tempY) {
        return !view.getBoard()[tempX - 1][tempY - 1].isFree() || xCoordinates.contains(tempX) && yCoordinates.contains(tempY);
    }

    private boolean isOutOfBounds(Integer coordinate) {
        return coordinate < 1 || coordinate > 5;
    }
}

