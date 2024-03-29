package it.polimi.ingsw.view;

import it.polimi.ingsw.view.clientView.LoginViewState;
import it.polimi.ingsw.view.clientViewCLI.TerminalEventHandler;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which represents the JavaFX entrypoint.
 * It acts as a storage of the gameState (lightModel).
 * Decoupling has been deemed unnecessary as the information required for rendering is minimal.
 */
public class View extends Application {
    private Socket clientSocket;
    private ViewState viewState;
    private FieldCell[][] board;
    private List<Integer> availableCellsX;
    private List<Integer> availableCellsY;
    private List<String> availableGods;
    private LocalDateTime pingTimestamp;
    private Map<String, List<String>> playerInfos;
    private String myName;
    private Boolean isGameOver;
    private static String rendererChoice;

    public static void launchView() {
        launch();
    }

    /**
     * This function is used to get the view started, it creates a loginView state and launches it
     *
     * @param stage The stage you want the client to be shown
     */
    @Override
    public void start(Stage stage) {
        Platform.setImplicitExit(false); //When the stage is closed, the thread keeps running in background - necessary to make the CLI work on the JavaFX thread
        //stage.setFullScreen(true); TODO Should users be able to select it?
        stage.setOnCloseRequest(event -> handleCloseRequest(stage));
        this.viewState = new LoginViewState(stage, clientSocket, this, null);

        Thread CLIListener = new Thread(new TerminalEventHandler(this));
        CLIListener.start();

        this.viewState.next(ReceivedViewState.LOGIN_VIEW_STATE);

    }

    /**
     * This function is used to set infos of the players, those will be used to show player's info in the view
     *
     * @param player List of the player's nickname
     * @param god List of the player's god chosen
     * @param color List of the player's color chosen
     */

    public void setPlayerInfos(List<String> player, List<String> god, List<String> color) {
        this.playerInfos = new HashMap<>();

            for(int i = 0; i < player.size(); i++){
                List<String> godsAndColor = new ArrayList<>();
                godsAndColor.add(god.get(i));
                godsAndColor.add(color.get(i));
                this.playerInfos.put(player.get(i), godsAndColor);
            }
    }

    public Map<String, List<String>> getPlayerInfos(){
        return this.playerInfos;
    }

    public ViewState getViewState() {
        return viewState;
    }

    public void setViewState(ViewState viewState) {
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

    public void setMyName(String username) {
        this.myName = username;
    }

    public String getMyName(){
        return this.myName;
    }

    public String getRendererChoice() {
        return rendererChoice;
    }

    public static void setRendererChoice(String rendererChoice) {
        View.rendererChoice = rendererChoice;
    }

    public List<String> getAvailableGods() {
        return availableGods;
    }

    public void setAvailableGods(List<String> availableGods) {
        this.availableGods = availableGods;
    }


    public Boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(Boolean gameOver) {
        isGameOver = gameOver;
    }

    private void handleCloseRequest(Stage stage) {
        stage.close();
        Platform.exit();
        System.exit(0);
    }
}
