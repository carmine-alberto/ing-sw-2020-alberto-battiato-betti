package it.polimi.ingsw;

import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.cview.clientView.LoginView;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String, List<String>> playerInfos;
    private String myName;


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

    public void setPlayerInfos(List<String> player, List<String> god, List<String> color) {
        this.playerInfos = new HashMap<String, List<String>>();

            for(int i = 0; i < player.size(); i++){
                List<String> godsAndColor = new ArrayList<>();
                godsAndColor.add(god.get(i));
                godsAndColor.add(color.get(i));
                this.playerInfos.put(player.get(i), godsAndColor);
            }
    }

    public HashMap<String, List<String>> getPlayerInfos(){
        return this.playerInfos;
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

    public void setMyName(String username) {
        this.myName = username;
    }

    public String getMyName(){
        return this.myName;
    }
}
