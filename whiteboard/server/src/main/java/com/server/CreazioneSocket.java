package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CreazioneSocket {
    public static int PORT = 3000;
    private static ArrayList<Message> board = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server in ascolto sulla porta: " + PORT);

        while (true) {
            Socket s = ss.accept();
            System.out.println("Client: " + s.getInetAddress());

            Main client = new Main(s, board);
            client.start();
        }
    }
}
