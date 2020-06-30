package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.View;
import it.polimi.ingsw.cview.ReceivedEvent;

import java.util.Scanner;

import static it.polimi.ingsw.GameSettings.CLI;

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
            if (view.getRendererChoice().equals(CLI)) {
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
        ReceivedEvent event = stringToEnum(classPath[classPath.length - 1].replace(CLI, ""));
        view.getViewState().next(event);
        view.getViewState().showStage();
    }
    private ReceivedEvent stringToEnum (String event){
        event = event
                .replaceAll("(.)([A-Z])", "$1_$2")
                .toUpperCase();
        return ReceivedEvent.valueOf(event);
    }
}
