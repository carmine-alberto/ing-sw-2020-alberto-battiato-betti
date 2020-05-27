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
    private Thread selectionThread;
    private final Integer BOARD_SIZE = 5;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private String selectedColour;

    public BoardViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);

        FieldCell[][] newBoard = new FieldCell[BOARD_SIZE][BOARD_SIZE];
        for (Integer i = 0; i < BOARD_SIZE; i++)
            for (Integer j = 0; j < BOARD_SIZE; j++)
                newBoard[i][j] = new FieldCell(null, i, j);
        client.setBoard(newBoard);

        client.setAvailableCellsX(new ArrayList<>());
        client.setAvailableCellsY(new ArrayList<>());
    }

    @Override
    public void render() {
        FieldCell[][] boardRep = client.getBoard();
        showBoard(boardRep);

        if (selectionThread == null) {
            selectionThread = new Thread(() -> {
                Scanner input = new Scanner(System.in);


            });
            selectionThread.start();
        }
    }

    private void showBoard(FieldCell[][] board) {
/*      Esempio di scacchiera a video

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
        System.out.println("This is the current game board\n");
        System.out.println("Each letter represents a cell. Legend: \n" +
                "The o letter represents a cell without any worker or dome\n" +
                "The w letter represents a cell with a worker\n" +
                "The d letter represents a cell with a dome\n" +
                "The number next to each cell represents its height (dome excluded)\n\n\n" +
                client.getMyName() + ", we remind you that your color is " + client.getPlayerInfos().get(client.getMyName()).get(1));

        String color = "";
        FieldCell x;


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

    private void getInput(Player callee, Game currentGame) {
        Scanner input = new Scanner(System.in);

        String sel = input.nextLine();
        notify(new UserInputEvent(sel));
    }

}
