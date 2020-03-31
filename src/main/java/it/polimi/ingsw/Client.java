package it.polimi.ingsw;

import it.polimi.ingsw.view.clientView.LoginView;
import it.polimi.ingsw.view.clientView.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Client extends Application {
    private Socket clientSocket;
    private View viewState;

    public static void main(String[] args) {
        System.out.println( "Jacopo Gay lol" );
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //stage.setFullScreen(true);
        this.viewState = new LoginView(stage, clientSocket, viewState);
        viewState.render();

    }
}
