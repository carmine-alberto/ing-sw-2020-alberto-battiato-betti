package it.polimi.ingsw;

import static it.polimi.ingsw.GameSettings.*;

/**
 * Hello world!
 *
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("Ce la faremo! " + Client.class.getClassLoader().getResource("copertina_santorini_2016.jpg"));

        if (args.length > 0 && args[FIRST_ELEMENT].equals("-" + CLI))
            View.setRendererChoice(args[FIRST_ELEMENT].substring(1));
        else
            View.setRendererChoice(GUI);

        View.launchView();

    }
}