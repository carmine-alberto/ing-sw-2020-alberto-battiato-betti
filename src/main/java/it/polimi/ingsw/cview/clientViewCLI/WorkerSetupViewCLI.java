package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.controller.events.WorkerSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import it.polimi.ingsw.model.FieldCell;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkerSetupViewCLI extends CLIView {
    private Thread selectionThread;
    private Boolean hasPlayerAlreadyChosen;
    private final Integer BOARD_SIZE = 5;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private String selectedColour;

    public WorkerSetupViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
        hasPlayerAlreadyChosen = false;
    }

    @Override
    public void render() {
        FieldCell[][] boardRep = client.getBoard();
        if (boardRep != null) {
            showBoard(boardRep);

            if (!hasPlayerAlreadyChosen) {
                hasPlayerAlreadyChosen = true;
                new Thread(() -> {
                    Scanner input = new Scanner(System.in);
                    List<Integer> xCoordinates = new ArrayList<>();
                    List<Integer> yCoordinates = new ArrayList<>();

                    askForPreferredColor(input);

                    askForWorkersPosition(input, xCoordinates, yCoordinates);

                    notify(new WorkerSelectionEvent(xCoordinates, yCoordinates, selectedColour));
                })
                .start();
            }
        }
    }

    private void askForPreferredColor(Scanner input) {
        Boolean isColourValid;

        do {
            isColourValid = true;
            CLIFormatter.print("Choose a color for your workers (available: red, green, yellow): ");
            selectedColour = input.next();

            if (!List.of("RED", "GREEN", "YELLOW").contains(selectedColour.toUpperCase())) {
                CLIFormatter.print("The selected color is not available, try again");
                isColourValid = false;
            }

            for (Integer i = 0; i < BOARD_SIZE && isColourValid; i++)
                for (Integer j = 0; j < BOARD_SIZE && isColourValid; j++)
                    if (client.getBoard()[i][j].getWorker() != null && client.getBoard()[i][j].getWorker().getOwner().getColour().toUpperCase().equals(selectedColour.toUpperCase())) {
                        CLIFormatter.print("One of your opponents already chose this color, pick another one!");
                        isColourValid = false;
                    }
        } while (!isColourValid);
    }

    private void askForWorkersPosition(Scanner input, List<Integer> xCoordinates, List<Integer> yCoordinates) {
        Integer tempX;
        Integer tempY;

        for (Integer i = 1; i < 3; i++) {
            do {
                CLIFormatter.print("Select the position of the worker n°" + i + " .\nX (from 1 to 5): ");
                tempX = input.nextInt();
                CLIFormatter.print("Y (from 1 to 5): ");
                tempY = input.nextInt();
            } while (isOutOfBounds(tempX) || isOutOfBounds(tempY) || isOccupied(tempX, tempY));
            xCoordinates.add(tempX);
            yCoordinates.add(tempY);
        }

    }

    private Boolean isOccupied(Integer tempX, Integer tempY) {
        return !client.getBoard()[tempX - 1][tempY - 1].isFree();
    }

    private boolean isOutOfBounds(Integer coordinate) {
        return coordinate < 1 || coordinate > 5;
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
        System.out.println("This is the current game board\n\n");
        System.out.println("Each letter represents a cell. Legend: \n" +
                "The o letter represents a cell without any worker or dome\n" +
                "The w letter represents a cell with a worker\n" +
                "The d letter represents a cell with a dome\n" +
                "The number next to each cell represents its height (dome excluded)\n\n\n");

        String color = "";
        FieldCell x;


        System.out.print(" ");
        for (int c = 1; c <= BOARD_SIZE; c++)
            System.out.print("  " + c);

        System.out.print("\n" + " ╔");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╗");

        for(int i = 1; i < BOARD_SIZE; i++, System.out.println(" ║")) {
            System.out.print(i + "║ ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                x = board[i][j];
                if (x.isFree())
                    System.out.print("o" + board[i][j].getHeight() + " ");
                else if (x.getHasDome())
                    System.out.print("d" + board[i][j].getHeight() + " ");
                else {
                    color = x.getWorker().getOwner().getColour();
                    switch (color.toUpperCase()) {
                        case "RED":
                            System.out.print(ANSI_RED + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        case "GREEN":
                            System.out.print(ANSI_GREEN + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        case "YELLOW":
                            System.out.print(ANSI_YELLOW + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                    }
                }

            }
        }
        System.out.print("╚");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╝");
    }

}

