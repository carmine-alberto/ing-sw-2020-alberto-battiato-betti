package it.polimi.ingsw.cview;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.UserInputEvent;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.cview.utility.ChoiceWindow;
import it.polimi.ingsw.cview.utility.MessageWindow;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.List;

public abstract class  View {
    protected static Integer BOARD_SIZE = 5;
    protected Stage mainStage;
    protected Socket clientSocket;
    protected Client client;
    protected View viewState; //TODO Remove useless attribute - use getter and setter instead
    protected VirtualView virtualView;
    protected ObjectOutputStream out;

    public View() {};

    public View(VirtualView virtualView, Socket clientSocket, View viewState) {
        this.virtualView = virtualView;
        this.clientSocket = clientSocket;
        this.viewState = viewState;
    }

    public View(Stage stage, Socket clientSocket, Client client, ObjectOutputStream out) {
        this.mainStage = stage;
        this.clientSocket = clientSocket;
        this.client = client;
        this.out = out;
    }

    /**
     * This function is used to start the view
     */
    public abstract void render();

    /**
     * This function is used to start the view
     */
    public abstract void terminate();

    /**
     * This function is used to pass to the next view
     *
     * @param nextState The next state you want to enter to (red in the phasesTree)
     */
    public void next(String nextState) {
        Platform.runLater(() -> {
            try {
                String rendererChoice = client.getRendererChoice();
                if (rendererChoice.equals("CLI"))  //TODO So awful if left here alone - needed for LoginView: if the username is rejected, we need the stage to be open in order to perform a second attempt.
                    mainStage.close();
                View newState = (View) Class.forName("it.polimi.ingsw.cview.clientView" + rendererChoice + "."  +  nextState + rendererChoice)
                        .getConstructors()[0]
                        .newInstance(mainStage, clientSocket, client, out);
                client.setViewState(newState);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //TODO Handle exception properly
            }
            client.getViewState().render();
        });
    }

    /**
     * Used to send an event to the server
     *
     * @param e the event you want to send
     */
    public void notify(Event e) {
        Platform.runLater(() -> {
            try {
                out.writeObject(e);
                out.reset();
            } catch (IOException ex) {
                connectionClosedHandler();
            }
        });
    }

    /**
     * This method is used whenever the client disconnects from the server
     */
    protected abstract void connectionClosedHandler();

    /**
     * This method shows the given message to the player
     * @param message You want to be shown
     */
    public abstract void showMessage(String message);

    /**
     * This method shows the given warning to the player
     * @param message You want to be shown
     */
    public abstract void showWarning(String message);

    /**
     * This method shows the given choices to the player
     * @param availableChoices You want to be shown
     */
    public abstract void showChoices(List<String> availableChoices);

}
