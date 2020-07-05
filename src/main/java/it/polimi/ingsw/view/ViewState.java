package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.view.clientView.*;
import it.polimi.ingsw.view.clientViewCLI.*;
import it.polimi.ingsw.view.serverView.VirtualView;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static it.polimi.ingsw.GameSettings.CLI;

public abstract class ViewState {
    protected Stage mainStage;
    protected Socket clientSocket;
    protected View view;
    protected VirtualView virtualView;
    protected ObjectOutputStream out;

    public ViewState() {}

    public ViewState(VirtualView virtualView) {
        this.virtualView = virtualView;
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
     * @param reason
     */
    public void terminate(String reason) {
        view.setGameOver(true);
    }

    /**
     * This function is used to pass to the next viewState
     *
     * @param nextState An enumeration representing the next state you want to enter
     */
    public void next(ReceivedViewState nextState) {
        Platform.runLater(() -> {
            ViewState newState = new BoardViewState(mainStage, clientSocket, view, out);
            String rendererChoice = view.getRendererChoice();
            if (rendererChoice.equals(CLI))  //Needed for LoginView: if the username is rejected, we need the stage to be open in order to perform a second attempt.
                mainStage.close();

            if (CLI.equals(rendererChoice)) {
                switch (nextState) {
                    case BOARD_VIEW_STATE -> {
                        newState = new BoardViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                    case CHALLENGER_SELECTION_VIEW_STATE -> {
                        newState = new ChallengerSelectionViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                    case GOD_POWER_VIEW_STATE -> {
                        newState = new GodPowerViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                    case LOGIN_VIEW_STATE -> {
                        newState = new LoginViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                    //case TERMINAL_EVENT_HANDLER -> newState = new TerminalEventHandler(mainStage, clientSocket, view, out); //TODO ?
                    case WAITING_VIEW_STATE -> {
                        newState = new WaitingViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                    case WORKER_SETUP_VIEW_STATE -> {
                        newState = new WorkerSetupViewStateCLI(mainStage, clientSocket, view, out);
                        break;
                    }
                }
            } else {//GUI
                switch (nextState) {
                    case BOARD_VIEW_STATE -> {
                        newState = new BoardViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                    case CHALLENGER_SELECTION_VIEW_STATE -> {
                        newState = new ChallengerSelectionViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                    case GOD_POWER_VIEW_STATE -> {
                        newState = new GodPowerViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                    case LOGIN_VIEW_STATE -> {
                        newState = new LoginViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                    case VIEW_STATE3_D -> {
                        newState = new ViewState3D(mainStage, clientSocket, view, out);
                        break;
                    }
                    case WAITING_VIEW_STATE -> {
                        newState = new WaitingViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                    case WORKER_SETUP_VIEW_STATE -> {
                        newState = new WorkerSetupViewState(mainStage, clientSocket, view, out);
                        break;
                    }
                }
            }

           /*       WITH REFLECTION
           try {
                String rendererChoice = view.getRendererChoice();
                if (rendererChoice.equals(CLI))  //TODO So awful if left here alone - needed for LoginView: if the username is rejected, we need the stage to be open in order to perform a second attempt.
                    mainStage.close();
                ViewState newState = (ViewState) Class.forName(ViewState.class.getPackageName() + ".clientView" + rendererChoice + "."  +  nextState  + rendererChoice)
                        .getConstructors()[0]
                        .newInstance(mainStage, clientSocket, view, out);
                view.setViewState(newState);
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //TODO Handle exception properly
            }
            */

            if (mainStage.getScene() == null) {
                mainStage.setScene(new Scene(new Group(), 700, 600));
            }
            view.setViewState(newState);
            view.getViewState().render();
        });
    }

    /**
     * Creates a connection to the server and, if successful, launches the thread that will handle events received from the server
     * @param serverIP A String representing the server IP
     * @param serverPort An Integer representing the server port
     * @throws IOException when the connection is refused for whatever reason
     */
    protected void setupConnectionToServer(String serverIP, Integer serverPort) throws IOException {
        clientSocket = new Socket(serverIP, serverPort);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

        Thread serverListener = new Thread(new ViewEventHandler(view, clientInputStream));
        serverListener.start();
    }

    /**
     * Sends an event to the server
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
     * Shows the stage on the screen
     */
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
