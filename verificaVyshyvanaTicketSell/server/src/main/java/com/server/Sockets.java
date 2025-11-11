package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sockets {
    public static int PORT = 3000;
    public static Ticket ticket = new Ticket(10, 30, 60);
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server in ascolto sulla porta: " + PORT);

        while (true) {
            Socket s = ss.accept();
            System.out.println("Client: " + s.getInetAddress());

            Main client = new Main(s, ticket);
            client.start();
        }
    }
}
