package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyThread extends Thread {
    private Socket socket;

    public MyThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Welcome: v2.2"); 
            while (true) {
                String mess = in.readLine();
                if (mess == null || mess.equals("!")) {
                    break;
                }

                mess = mess.toUpperCase();
                out.println(mess);
            }

            System.out.println("Connessione chiusa con " + socket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
