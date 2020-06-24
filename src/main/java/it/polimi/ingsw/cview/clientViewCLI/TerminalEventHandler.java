package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;

import java.util.Scanner;

public class TerminalEventHandler implements Runnable {
    private Scanner in;
    private View view;
    private static String SWITCH = "switch";

    public TerminalEventHandler(View view) {
        this.in = new Scanner(System.in);
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            String nextLine = in.nextLine();
            if (view.getRendererChoice().equals("CLI")) {
                if (nextLine.equals(SWITCH)) {
                    switchToGUI();
                } else {
                    ((CLIViewState) view.getViewState()).handleCLIInput(nextLine);
                    view.getViewState().render();
                }
            }
        }
    }

    private void switchToGUI() {
        view.setRendererChoice("");

        String[] classPath = view.getViewState().getClass().getName().split("\\.");
        view.getViewState().next(classPath[classPath.length - 1].replace("CLI", ""));
        view.getViewState().showStage();
    }
}
