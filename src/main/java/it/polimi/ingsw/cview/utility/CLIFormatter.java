package it.polimi.ingsw.cview.utility;

import java.util.List;

public class CLIFormatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static String formatStringList(List<String> listOfStrings) {
        return listOfStrings.toString();
    }

    public static void print(String printable) {
        System.out.println(printable);
    }
}
