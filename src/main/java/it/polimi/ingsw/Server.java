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

/**
 * Server entrypoint: this is the first class to be run when the server is launched.
 * The port can be specified from the command line using the syntax "-port PORT_NUMBER".
 * If no parameter is present, the default port is used (see GameSettings, DEFAULT_PORT)
 */
public class Server {
    private Game currentGame;
    private Controller controller;
    private ServerSocket serverSocket;
    private ExecutorService executorThreadPool;
    private Integer port;

    public static void main(String[] args){
        Server gameServer = new Server();
        gameServer.currentGame  = new Game();
        gameServer.controller = new Controller(gameServer.currentGame);

        try {
            if (args.length == 2 && args[0].equals("-port"))
                gameServer.port = Integer.parseInt(args[1]);
            else
                gameServer.port = DEFAULT_PORT_NUMBER;
            gameServer.start(gameServer.port);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            gameServer.listenToNewGameConnections(gameServer.port);
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
     * @param port
     */
    private void listenToNewGameConnections(Integer port) throws IOException {
        while (true) {
                System.out.println("Server listening on port " + port + " ...");
                Socket socket = serverSocket.accept();
                VirtualView connectedView = new VirtualView(socket, controller);

                executorThreadPool.submit(connectedView);
        }
    }
}

