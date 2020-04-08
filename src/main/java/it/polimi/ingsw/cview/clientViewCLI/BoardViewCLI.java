package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.FieldCell;

public class BoardViewCLI extends View {

    final Integer BOARD_SIZE = 5;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public void render() {
        FieldCell[][] boardRep = client.getBoard();
        showBoard(boardRep);

    }

    private void showBoard(FieldCell[][] board) {
/*                      Esempio di scacchiera a video
        o0 o0 o0 o0 o0
        o0 o0 o0 o0 o0
        o0 o0 o0 o0 o0
        o0 o0 o0 o0 o0
        o0 o0 o0 o0 o0
*/
        System.out.println("This is the current game board\n\n");
        System.out.println("Each letter represent a cell. Legend: \n" +
                "The o letter represent a cell without any worker or dome\n" +
                "The w letter represent a cell wit a worker\n" +
                "The d letter represent a cell wit a dome\n" +
                "The number next to each cell represent his height (dome excluded)\n\n\n");

        String color = "";
        FieldCell x;

        for(int i = 1; i <= BOARD_SIZE; i++, System.out.println("\n"))
            for(int j = 0; j <= BOARD_SIZE; j++){
                x = board[i][j];
                if (x.isFree())
                    System.out.println("o" + board[i][j].getHeight() + " ");
                else
                    if (x.getHasDome())
                        System.out.println("d" + board[i][j].getHeight() + " ");
                    else
                        color = x.getWorker().getOwner().getColour();
                        switch (color) {
                            case "Red":
                                System.out.println(ANSI_RED + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                            case "Green":
                                System.out.println(ANSI_GREEN + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                            case "Yellow":
                                System.out.println(ANSI_YELLOW + "w" + ANSI_RESET + board[i][j].getHeight() + " ");
                        }
            }
    }
}
