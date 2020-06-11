package it.polimi.ingsw.cview;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.serverView.VirtualView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.List;

public abstract class ViewState {
    protected static Integer BOARD_SIZE = 5;
    protected Stage mainStage;
    protected Socket clientSocket;
    protected View view;
    protected ViewState viewState; //TODO Remove useless attribute - use getter and setter instead
    protected VirtualView virtualView;
    protected ObjectOutputStream out;

    public ViewState() {};

    public ViewState(VirtualView virtualView, Socket clientSocket, ViewState viewState) {
        this.virtualView = virtualView;
        this.clientSocket = clientSocket;
        this.viewState = viewState;
    }

    public ViewState(Stage stage, Socket clientSocket, View view, ObjectOutputStream out) {
        this.mainStage = stage;
        this.clientSocket = clientSocket;
        this.view = view;
        this.out = out;
    }

    public abstract void render();

    public abstract void terminate();

    public void next(String nextState) {
        Platform.runLater(() -> {
            try {
                String rendererChoice = view.getRendererChoice();
                if (rendererChoice.equals("CLI"))  //TODO So awful if left here alone - needed for LoginView: if the username is rejected, we need the stage to be open in order to perform a second attempt.
                    mainStage.close();
                ViewState newState = (ViewState) Class.forName("it.polimi.ingsw.cview.clientView" + rendererChoice + "."  +  nextState + "State" + rendererChoice)
                        .getConstructors()[0]
                        .newInstance(mainStage, clientSocket, view, out);
                view.setViewState(newState);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //TODO Handle exception properly
            }
            view.getViewState().render();
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
