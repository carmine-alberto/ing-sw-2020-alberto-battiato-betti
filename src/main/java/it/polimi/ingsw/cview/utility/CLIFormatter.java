package it.polimi.ingsw.cview.utility;

import java.util.List;

public class CLIFormatter {
    public static String formatStringList(List<String> listOfStrings) {
        return listOfStrings.toString();
    }

    public static void print(String printable) {
        System.out.println(printable);
    }
}
