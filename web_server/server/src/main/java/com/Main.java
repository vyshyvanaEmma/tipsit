package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static int PORT = 3000;

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT);
            while (true) {
                Socket socket = ss.accept();
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        }
    }
}
