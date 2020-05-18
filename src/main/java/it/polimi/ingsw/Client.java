package it.polimi.ingsw;

import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.clientView.LoginView;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Client extends Application {
    private Socket clientSocket;
    private View viewState;
    private FieldCell[][] board;
    private List<Integer> availableCellsX;
    private List<Integer> availableCellsY;
    private LocalDateTime pingTimestamp;


    public static void main(String[] args) {
        System.out.println( "Ce la faremo!");

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

    public List<Integer> getAvailableCellsX() {
        return availableCellsX;
    }

    public void setAvailableCellsX(List<Integer> availableCellsX) {
        this.availableCellsX = availableCellsX;
    }

    public List<Integer> getAvailableCellsY() {
        return availableCellsY;
    }

    public void setAvailableCellsY(List<Integer> availableCellsY) {
        this.availableCellsY = availableCellsY;
    }

    public LocalDateTime getPingTimestamp() {
        return pingTimestamp;
    }

    public void setPingTimestamp(LocalDateTime pingTimestamp) {
        this.pingTimestamp = pingTimestamp;
    }
}
