package it.polimi.ingsw;

import it.polimi.ingsw.view.LoginView;
import it.polimi.ingsw.view.View;
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
        this.viewState = new LoginView(stage, clientSocket, viewState);
        viewState.render();

    }
}
