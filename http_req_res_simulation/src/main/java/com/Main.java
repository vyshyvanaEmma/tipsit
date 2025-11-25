package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static int PORT = 3000;
    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            Socket s = ss.accept();

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String request;
            do {
                request = in.readLine();
                System.out.println(request);
            } while (!request.isEmpty());

            String response = "Buongiorno admin!";
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println("Content-Length: " + response.length());
            out.println("");
            out.println(response);

            s.close();
        }
    }
}