package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.utility.MessageBox;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public abstract class  View {
    protected Stage mainStage;
    protected Socket clientSocket;
    protected View viewState;

    public View(Stage stage, Socket clientSocket, View viewState) {
        this.mainStage = stage;
        this.clientSocket = clientSocket;
        this.viewState = viewState;
    }

    public abstract void render();


    public void next(String nextState) {
        try {
            viewState = (View) Class.forName("it.polimi.ingsw.view.clientView." + nextState)
                    .getConstructors()[0]
                    .newInstance(mainStage, clientSocket, viewState);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); //TODO Handle exception properly
        }
        
        viewState.render();
    }


    protected void connectionClosedHandler() {
        MessageBox.show("Connection closed - Restart the client and try again!", "Connection Error");
        mainStage.close();
    }

}
