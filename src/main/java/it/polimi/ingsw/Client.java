package it.polimi.ingsw;

import it.polimi.ingsw.view.View;

import static it.polimi.ingsw.GameSettings.*;

/**
 * App entrypoint: this is the first class to be run in order to launch the client.
 * The renderingMode can be specified from the command line:
 * - adding "-CLI" results in a CommandLine-based game experience;
 * - adding nothing results in the GUI being launched
 */
public class Client {

    public static void main(String[] args) {
        //System.out.println("Ce la faremo! ");

        if (args.length > 0 && args[FIRST_ELEMENT_INDEX].equals("-" + CLI))
            View.setRendererChoice(args[FIRST_ELEMENT_INDEX].substring(1));
        else
            View.setRendererChoice(GUI);

        View.launchView();

    }
}