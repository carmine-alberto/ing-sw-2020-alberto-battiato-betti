package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.controller.events.ChallengerSelectionEvent;
import it.polimi.ingsw.cview.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChallengerSelectionViewCLI extends View {

    private List<String> godsList;

    //todo constructors?

    @Override
    public void render() {

        Scanner input = new Scanner(System.in);
        int selectedNumber = -1;
        List<String> selectedGods = new ArrayList<String>();
        int selectedStarter;

        do {
            if (selectedNumber != -1) {
                System.out.println("Enter the number of the players (only 2 and 3 players game available at the moment):");
            }
            selectedNumber = input.nextInt();
        } while(selectedNumber != 2 && selectedNumber != 3);



        System.out.println("Which one of these Gods do you want to play with" +
                godsList + "?\n" +
                "(Enter" + selectedNumber +"Gods)\n");

        for(int i = 0; i < selectedNumber; i++) {
            String in= input.next();
            if (godsList.contains(in)) {
                selectedGods.add(in);
                godsList.remove(in);  //si può scegliere una sola volta un godpower
                if(selectedNumber-i != 0)
                    System.out.println("Perfect. Enter" + (selectedNumber-i) + "more Gods");
            } else {
                System.out.println("The God you chose wasn't in the list. Please select one of these Gods:\n" + godsList);
                i--;
            }
        }



        System.out.println("Select the staring player: ");
        if(selectedNumber == 2)
            System.out.println("1 or 2 ?");
        else
            System.out.println("1, 2 or 3?");
        selectedStarter = input.nextInt();


        notify(new ChallengerSelectionEvent(selectedNumber, selectedGods, selectedStarter));
    }
}
