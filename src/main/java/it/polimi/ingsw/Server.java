package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static List<Game> games;
    static Controller controller;


    public static void main(String[] args){
        games = new ArrayList<>();
        games.add(new Game());
        controller = new Controller(games.get(0));
        startServer(1200);
    }

    public static void startServer(int port) {
        Game lastGame = games.get(games.size() - 1);
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Port not available
            return;
        }

        System.out.println("Server ready");

        while (lastGame.getPlayers().size() < lastGame.NUM_OF_PLAYERS) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new VirtualView(socket, controller));
            } catch (IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        
        executor.shutdown();
    }
}

