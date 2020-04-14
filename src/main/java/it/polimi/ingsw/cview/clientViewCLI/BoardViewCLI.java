package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.*;

import java.util.Scanner;

public class BoardViewCLI extends View {

    private final Integer BOARD_SIZE = 5;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private Player callee;
    private Game currentGame;

    @Override
    public void render() {
        FieldCell[][] boardRep = client.getBoard();
        showBoard(boardRep);
        if(currentGame.getTurnPlayer().equals(callee))
            getInput(callee, currentGame);

    }

    private void showBoard(FieldCell[][] board) {
/*                      Esempio di scacchiera a video

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
        System.out.println("Each letter represent a cell. Legend: \n" +
                "The o letter represent a cell without any worker or dome\n" +
                "The w letter represent a cell wit a worker\n" +
                "The d letter represent a cell wit a dome\n" +
                "The number next to each cell represent his height (dome excluded)\n\n\n");

        String color = "";
        FieldCell x;


        System.out.print(" ");
        for (int c = 1; c <= BOARD_SIZE; c++)
            System.out.print("  " + c);

        System.out.print("\n" + " ╔");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╗");

        for(int i = 1; i <= BOARD_SIZE; i++, System.out.println(" ║")) {
            System.out.print(i + "║ ");
            for (int j = 0; j <= BOARD_SIZE; j++) {
                x = board[i][j];
                if (x.isFree())
                    System.out.print("o" + board[i][j].getHeight() + " ");
                else if (x.getHasDome())
                    System.out.print("d" + board[i][j].getHeight() + " ");
                else
                    color = x.getWorker().getOwner().getColour();
                switch (color) {
                    case "Red":
                        System.out.print(ANSI_RED + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                    case "Green":
                        System.out.print(ANSI_GREEN + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                    case "Yellow":
                        System.out.print(ANSI_YELLOW + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                }
            }
        }
        System.out.print("╚");
        for (int c = 1; c <= BOARD_SIZE + 3; c++)
            System.out.print("══");
        System.out.println("╝");
    }

    private void getInput(Player callee, Game currentGame) {
        Scanner input = new Scanner(System.in);

        String sel = input.nextLine();
        notify(new UserInputEvent(sel));
//        System.out.println("Enter the coordinates you wold like to move the worker to");
    }
//        System.out.println("Where would you like to build? Enter the coordinates");

}
