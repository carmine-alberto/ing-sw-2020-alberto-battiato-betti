package it.polimi.ingsw.cview;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.cview.utility.MessageBox;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public abstract class  View {
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
        this.mainStage.close();
    }


    public void next(String nextState) {
        Platform.runLater(() -> {
            try {
                View newState = (View) Class.forName("it.polimi.ingsw.cview.clientView." + nextState)
                        .getConstructors()[0]
                        .newInstance(mainStage, clientSocket, client, out);
                client.setViewState(newState);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //TODO Handle exception properly
            }
            System.out.println("viewstate set: " + this.getClass());
            client.getViewState().render();
        });
    }


    protected void connectionClosedHandler() {
        MessageBox.show("Connection closed - Restart the client and try again!", "Connection Error");
        mainStage.close();
    }

    protected void sendToServer(Event e) {
        try {
            out.writeObject(e);
        } catch (IOException ex) {
            connectionClosedHandler();
        }
    }
}