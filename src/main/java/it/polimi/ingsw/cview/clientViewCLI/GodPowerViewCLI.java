package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GodPowerViewCLI extends CLIView {
    private String selectedGod;

    public GodPowerViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }
    @Override
    public void render() {
        Thread selectionThread;
        List<String> availableGods = client.getAvailableGods();

        if (availableGods != null) {
            selectionThread = new Thread(() -> {
                Scanner input = new Scanner(System.in);

                askForDesiredGod(availableGods, input);

                notify(new GodSelectionEvent(selectedGod));
            });
            selectionThread.start();
        }
    }

    private void askForDesiredGod(List<String> availableGods, Scanner input) {
        do {
            CLIFormatter.print("God Powers available: " + CLIFormatter.formatStringList(availableGods) + "\n" +
                    "Enter the name of the desired God Power: ");
            selectedGod = input.next();
        } while (!availableGods.contains(selectedGod));
    }

}