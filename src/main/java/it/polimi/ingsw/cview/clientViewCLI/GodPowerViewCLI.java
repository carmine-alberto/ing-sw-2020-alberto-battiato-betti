package it.polimi.ingsw.cview.clientViewCLI;

import it.polimi.ingsw.controller.events.GodSelectionEvent;
import it.polimi.ingsw.cview.View;

import java.util.List;
import java.util.Scanner;

public class GodPowerViewCLI extends View {

    private List<String> godsList = List.of("Empty"); //in teoria lo dovrebbe condividere con la view GUI

    @Override
    public void render() {

        Scanner input = new Scanner(System.in);
        String selected = null;

        System.out.println("Enter the name of the desired God Power:");
        System.out.println(godsList);

        do {
            if (selected != null) {
                System.out.println("Enter the name of one of these God Power:");
                System.out.println(godsList);
            }
            selected = input.next();
        } while(!godsList.contains(selected));
        sendSelectionToServer(selected);
        removeFromGodsList(selected);
    }

    private void sendSelectionToServer(String selection){
        notify(new GodSelectionEvent(selection));

    }

    private void removeFromGodsList(String toDelete){
        godsList.remove(toDelete);
    }
}
