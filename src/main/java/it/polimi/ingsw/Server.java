package it.polimi.ingsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    public static void main(String[] args){
        startServer(1200);
    }

    public static void startServer(int port) {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ClientServerHandler(socket));
            } catch (IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }
}

