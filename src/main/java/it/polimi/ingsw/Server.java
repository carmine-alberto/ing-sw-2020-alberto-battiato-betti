package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.cview.serverView.VirtualView;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.GameSettings.FIRST_ELEMENT;
import static it.polimi.ingsw.GameSettings.PORT_NUMBER;

public class Server {
    static List<Game> games;
    static Game lastGame;
    static Controller controller;
    static ServerSocket serverSocket;
    static ExecutorService executor;
    public static Boolean acceptNextPlayer;


    public static void main(String[] args){
        games = new ArrayList<>();
        games.add(new Game());
        controller = new Controller(games.get(FIRST_ELEMENT));
        startServer(PORT_NUMBER);
        listenToNewGameConnections();
    }

    /**
     * This functions starts the server on the given server socket port and sets the executor
     *
     * @param port the port you want the server to work on
     */
    public static void startServer(int port) {
        executor = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Port not available
            return;
        }
    }

    /**
     * This function is used to set the server waiting for connections
     */
    private static void listenToNewGameConnections() {
        lastGame = games.get(games.size() - 1);
        while (true) {
            try {
                System.out.println("Server ready, players: " + lastGame.getPlayers().size() + " of " + lastGame.NUM_OF_PLAYERS);
                Socket socket = serverSocket.accept();
                executor.submit(new VirtualView(socket, controller));

            } catch (IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }
}

