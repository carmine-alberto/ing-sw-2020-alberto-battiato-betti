package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientServerHandler implements Runnable {

        private Socket socket;

        public ClientServerHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                ObjectOutputStream serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream serverInputStream = new ObjectInputStream(socket.getInputStream());

                String stringa = (String) serverInputStream.readObject(); // controllare stringa vuota
                System.out.println((stringa));

                serverOutputStream.close();
                serverInputStream.close();
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

