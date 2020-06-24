package it.polimi.ingsw.cview;

import it.polimi.ingsw.View;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.cview.serverView.VirtualView;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
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
                String rendererChoice = view.getRendererChoice();
                if (rendererChoice.equals("CLI"))  //TODO So awful if left here alone - needed for LoginView: if the username is rejected, we need the stage to be open in order to perform a second attempt.
                    mainStage.close();
                ViewState newState = (ViewState) Class.forName(ViewState.class.getPackageName() + ".clientView" + rendererChoice + "."  +  nextState  + rendererChoice)
                        .getConstructors()[0]
                        .newInstance(mainStage, clientSocket, view, out);
                view.setViewState(newState);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //TODO Handle exception properly
            }
            if (mainStage.getScene() == null) {
                mainStage.setScene(new Scene(new Group(), 700, 600));
            }
            view.getViewState().render();
        });
    }


    protected void setupConnectionToServer(String serverIP, Integer serverPort) throws IOException {
        clientSocket = new Socket(serverIP, serverPort);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

        Thread serverListener = new Thread(new ViewEventHandler(view, clientInputStream));
        serverListener.start();
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

    public void showStage() {
        Platform.runLater( () -> {
            mainStage.show();
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
