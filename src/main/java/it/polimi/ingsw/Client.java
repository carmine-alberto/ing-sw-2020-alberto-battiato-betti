package it.polimi.ingsw;

import static it.polimi.ingsw.GameSettings.*;

/**
 * Hello world!
 *
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("Ce la faremo! "); //TODO È il caso di toglierla ?

        if (args.length > 0 && args[FIRST_ELEMENT].equals("-" + CLI))
            View.setRendererChoice(args[FIRST_ELEMENT].substring(1));
        else
            View.setRendererChoice(GUI);

        View.launchView();

    }
}