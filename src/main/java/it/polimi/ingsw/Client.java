package it.polimi.ingsw;

import it.polimi.ingsw.cview.clientView.LoginView;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.FieldCell;
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
    private FieldCell[][] board;

    public static void main(String[] args) {
        System.out.println( "Jacopo Gay lol" );
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //stage.setFullScreen(true);
        this.viewState = new LoginView(stage, clientSocket, this);
        viewState.render();

    }

    public View getViewState() {
        return viewState;
    }

    public void setViewState(View viewState) {
        this.viewState = viewState;
    }

    public FieldCell[][] getBoard() {
        return board;
    }

    public void setBoard(FieldCell[][] board) {
        this.board = board;
    }
}
