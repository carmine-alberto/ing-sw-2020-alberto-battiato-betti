package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.serverView.VirtualView;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.GameSettings.*;

public class Server {
    private Game currentGame;
    private Controller controller;
    private ServerSocket serverSocket;
    private ExecutorService executorThreadPool;

    public static void main(String[] args){
        Server gameServer = new Server();
        gameServer.currentGame  = new Game();
        gameServer.controller = new Controller(gameServer.currentGame);

        try {
            gameServer.start(PORT_NUMBER);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        try {
            gameServer.listenToNewGameConnections();
        } catch (IOException e) {
            gameServer.executorThreadPool.shutdown();
            System.out.println(e.getMessage());
        }

    }

    /**
     * This functions starts the server on the given server socket port and sets the executor
     *
     * @param port the port you want the server to work on
     */
    private void start(int port) throws IOException {
        executorThreadPool = Executors.newCachedThreadPool();

        serverSocket = new ServerSocket(port);
    }

    /**
     * This function is used to set the server waiting for connections
     */
    private void listenToNewGameConnections() throws IOException {
        while (true) {
                System.out.println("Server listening...");
                Socket socket = serverSocket.accept();
                VirtualView connectedView = new VirtualView(socket, controller);

                executorThreadPool.submit(connectedView);
        }
    }
}

