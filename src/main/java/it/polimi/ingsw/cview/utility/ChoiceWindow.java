package it.polimi.ingsw.cview.utility;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ChoiceWindow {
    static Stage stage;
    static String choice;

    /**
     * This function is used to show a new window possible choices
     * @param title The new window title
     * @param choices The list of the possibilities
     * @return The choice chosen
     */
    public static String show(String title, List<String> choices) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        HBox choicesBox = new HBox(20);
        choicesBox.setPadding(new Insets(60));
        for (String choice: choices) {
            Button choiceButton = new Button(choice);
            choiceButton.setOnAction(e -> handleChoice((Button)e.getSource()));
            choicesBox.getChildren().add(choiceButton);
        }

        Scene scene = new Scene(choicesBox);

        stage.setScene(scene);
        stage.showAndWait();
        return choice;
    }

    private static void handleChoice(Button choiceButton) {
        stage.close();
        choice = choiceButton.getText();
    }
}

