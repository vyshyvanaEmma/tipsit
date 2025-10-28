package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server socket in ascolto sulla porta 3000");

        try (ServerSocket ss = new ServerSocket(3000)) {
            while (true) {
                Socket myS = ss.accept();
                System.out.println("Qualcuno si è collegato: " + myS.getInetAddress());

                MyThread t = new MyThread(myS);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Comunicazione terminata");
    }
}
