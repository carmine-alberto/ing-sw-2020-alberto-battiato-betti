package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;

import java.util.Scanner;

public class TerminalEventHandler implements Runnable {
    private Scanner in;
    private Client client;

    public TerminalEventHandler(Client client) {
        this.in = new Scanner(System.in);
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            String nextLine = in.nextLine();
            if (client.getRendererChoice().equals("CLI")) {
                ((CLIView) client.getViewState()).handleCLIInput(nextLine);
                client.getViewState().render();
            }
        }
    }
}
