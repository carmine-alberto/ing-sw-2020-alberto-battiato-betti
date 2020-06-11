package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;

import java.util.Scanner;

public class TerminalEventHandler implements Runnable {
    private Scanner in;
    private View view;

    public TerminalEventHandler(View view) {
        this.in = new Scanner(System.in);
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            String nextLine = in.nextLine();
            if (view.getRendererChoice().equals("CLI")) {
                ((CLIViewState) view.getViewState()).handleCLIInput(nextLine);
                view.getViewState().render();
            }
        }
    }
}
