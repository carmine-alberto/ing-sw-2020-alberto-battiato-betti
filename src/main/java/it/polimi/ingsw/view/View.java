package it.polimi.ingsw.view;

import it.polimi.ingsw.view.utility.MessageBox;
import javafx.stage.Stage;

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

    public abstract void next();

    protected void connectionClosedHandler() {
        MessageBox.show("Connection closed - Restart the client and try again!", "Connection Error");
        mainStage.close();
    }

}
