package it.polimi.ingsw.view.utility;

import java.util.List;

public class CLIFormatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_CYAN = "\033[36m";
    public static final String ANSI_MAGENTA = "\033[35m";
    public static final String ANSI_YELLOW = "\033[33m";

    public static String formatStringList(List<String> listOfStrings) {
        return listOfStrings.toString();
    }

    public static void print(String printable) {
        System.out.println(printable);
    }

    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
