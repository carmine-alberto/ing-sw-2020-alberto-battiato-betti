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

    public abstract void render();

    public void terminate() {
        Platform.runLater(() -> this.mainStage.close());
    }


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

    protected abstract void connectionClosedHandler();

    public abstract void showMessage(String message);

    public abstract void showWarning(String message);

    public abstract void showChoices(List<String> availableChoices);

}
