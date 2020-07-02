package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.cview.ViewState;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static it.polimi.ingsw.GameSettings.FIELD_SIZE;
import static it.polimi.ingsw.cview.utility.CLIFormatter.*;

public abstract class CLIViewState extends ViewState {

    public CLIViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        super(stage, clientSocket, view, out);
    }

    /**
     * This function is used whenever someone  closes the game connection
     */
    @Override
    protected void connectionClosedHandler() {
        CLIFormatter.print("Connection closed - Restart the client and try again!");
        System.exit(1);
    }

    /**
     * Shows general message
     * @param message You want to be shown
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Shows a warning
     * @param message You want to be shown
     */
    @Override
    public void showWarning(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * Shows some some possible choices
     * @param availableChoices You choices to be shown
     */
    @Override
    public void showChoices(List<String> availableChoices) {
        System.out.println("Pick one: " + CLIFormatter.formatStringList(availableChoices));
    }

    /**
     * This function is used to show the gameBoard
     * @param board
     */
    protected void showBoard(FieldCell[][] board) {
        /* Esempio di scacchiera a video

        1    2    3    4    5
         ╔════════════════════════╗
        1║ o0 ║ o0 ║ o0 ║ o0 ║ o0 ║
         ║════════════════════════║
        2║ o0 ║ o0 ║ o0 ║ o0 ║ o0 ║
         ║════════════════════════║
        3║ o0 ║ o0 ║ o0 ║ o0 ║ o0 ║
         ║════════════════════════║
        4║ o0 ║ o0 ║ o0 ║ o0 ║ o0 ║
         ║════════════════════════║
        5║ o0 ║ o0 ║ o0 ║ o0 ║ o0 ║
         ╚════════════════════════╝
        */
        Color color;
        FieldCell x;

        showMessage("""
                    This is the current game board
                    Each letter represents a cell. Legend:
                    The o letter represents a cell without any worker or dome
                    The w letter represents a cell with a worker
                    The d letter represents a cell with a dome
                    The number next to each cell represents its height (dome excluded)
                    """
        );

        System.out.print(" ");
        for (int c = 1; c <= FIELD_SIZE; c++)
            System.out.print("  " + c);

        System.out.print("\n" + " ╔");
        for (int c = 1; c <= FIELD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╗");

        for(int i = 0; i < FIELD_SIZE; i++, System.out.println("║")) {
            System.out.print((i + 1) + "║ ");
            for (int j = 0; j < FIELD_SIZE; j++) {
                x = board[i][j];
                if (x.isFree())
                    System.out.print("o" + board[i][j].getHeight() + " ");
                else{
                    if (x.getHasDome())
                        System.out.print("d" + board[i][j].getHeight() + " ");
                    else {
                        color = Color.valueOf(x.getWorker().getOwner().getColour());

                        if (Color.MAGENTA.equals(color)) {
                            System.out.print(ANSI_MAGENTA + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        } else if (Color.YELLOW.equals(color)) {
                            System.out.print(ANSI_YELLOW + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        } else if (Color.CYAN.equals(color)) {
                            System.out.print(ANSI_CYAN + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        }
                    }
                }
            }
        }
        System.out.print(" ╚");
        for (int c = 1; c <= FIELD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╝");
    }

    /**
     * Used when the game stops, to close the game
     */
    public void terminate() {
        Platform.exit();
        System.exit(0);
    }

    public abstract void handleCLIInput(String input);
}
