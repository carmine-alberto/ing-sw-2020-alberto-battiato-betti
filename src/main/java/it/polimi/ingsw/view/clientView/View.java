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
        viewState = switch(nextState) {
            case "ChallengerSelectionView" -> new ChallengerSelectionView(mainStage, clientSocket, viewState);
            case "WaitingView" -> new WaitingView(mainStage, clientSocket, viewState);
            case "GodPowerView" -> new GodPowerView(mainStage, clientSocket, viewState);
            case "MainView" -> new MainView(mainStage, clientSocket, viewState);
            default -> null;
        };

        //(View) Class.forName(nextState).getConstructor().newInstance(mainStage, clientSocket, viewState); --- alternativa allo switch

        viewState.render();
    }


    protected void connectionClosedHandler() {
        MessageBox.show("Connection closed - Restart the client and try again!", "Connection Error");
        mainStage.close();
    }

}
