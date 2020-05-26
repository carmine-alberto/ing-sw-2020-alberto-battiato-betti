package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.utility.CLIFormatter;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChallengerSelectionViewCLI extends View {

    private Integer selectedNumber;
    private Integer selectedStarter;
    private List<String> selectedGods;

    public ChallengerSelectionViewCLI(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        super(stage, clientSocket, client, out);
    }

    @Override
    public void render() {
        Thread selectionThread;
        List<String> availableGods = client.getAvailableGods();

        if (availableGods != null) {
            selectionThread = new Thread(() -> {
                Scanner input = new Scanner(System.in);
                selectedGods = new ArrayList<>();

                askForNumberOfPlayers(input);

                askForSelectedGods(availableGods, input);

                askForStarter(input);

                notify(new ChallengerSelectionEvent(selectedNumber, selectedGods, selectedStarter));
            });
            selectionThread.start();
        }
    }

    private void askForSelectedGods(List<String> availableGods, Scanner input) {
        CLIFormatter.print("Which of these Gods do you want to play with?\n\n");

        for (Integer i = 0; i < selectedNumber; i++) {
            CLIFormatter.print(
                "Available: " + CLIFormatter.formatStringList(availableGods) + "\n\n" +
                (selectedNumber - i) + " left to choose: "
            );

            String in = input.next();
            if (availableGods.contains(in)) {
                selectedGods.add(in);
                availableGods.remove(in);
            } else {
                System.out.println("The God you chose wasn't in the list. Please select one of the available Gods\n\n");
                i--;
            }
        }
    }

    private void askForStarter(Scanner input) {
        do {
            System.out.println("Select the starting player (1" + (selectedNumber == 2 ? " or 2)" : ", 2 or 3)"));
            selectedStarter = input.nextInt();
        } while (selectedStarter < 0 || selectedStarter > selectedNumber);
    }

    private void askForNumberOfPlayers(Scanner input) {
        do {
            System.out.println("Enter the number of the players (only 2 and 3 players game available at the moment): ");
            selectedNumber = input.nextInt();
        } while (selectedNumber != 2 && selectedNumber != 3);
    }
}