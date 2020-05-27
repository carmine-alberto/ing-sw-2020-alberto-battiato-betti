package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import it.polimi.ingsw.cview.utility.MessageWindow;
import it.polimi.ingsw.model.FieldCell;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static it.polimi.ingsw.cview.utility.CLIFormatter.*;

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
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    @Override
    public void showChoices(List<String> availableChoices) {
        System.out.println(CLIFormatter.formatStringList(availableChoices));
    }

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
        String color;
        FieldCell x;

        System.out.println("This is the current game board\n");
        System.out.println("Each letter represents a cell. Legend: \n" +
                "The o letter represents a cell without any worker or dome\n" +
                "The w letter represents a cell with a worker\n" +
                "The d letter represents a cell with a dome\n" +
                "The number next to each cell represents its height (dome excluded)\n");

        System.out.print(" ");
        for (int c = 1; c <= BOARD_SIZE; c++)
            System.out.print("  " + c);

        System.out.print("\n" + " ╔");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╗");

        for(int i = 0; i < BOARD_SIZE; i++, System.out.println("║")) {
            System.out.print((i + 1) + "║ ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                x = board[i][j];
                if (x.isFree())
                    System.out.print("o" + board[i][j].getHeight() + " ");
                else{
                    if (x.getHasDome())
                        System.out.print("d" + board[i][j].getHeight() + " ");
                    else {
                        color = x.getWorker().getOwner().getColour();
                        switch (color.toUpperCase()) {
                            case "RED":
                                System.out.print(ANSI_RED + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                                break;
                            case "GREEN":
                                System.out.print(ANSI_GREEN + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                                break;
                            default: //todo assign proper color when we don't find the precise one
                                //case "YELLOW":
                                System.out.print(ANSI_YELLOW + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                                break;
                        }
                    }
                }
            }
        }
        System.out.print(" ╚");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╝");
    }



    public abstract void handleCLIInput(String input);
}
