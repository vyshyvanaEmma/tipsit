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
        while(true){
            try (ServerSocket ss = new ServerSocket(PORT)) {
                Socket s = ss.accept();

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String primaRiga  = in.readLine();
                String request;
                do {
                    request = in.readLine();
                    System.out.println(request);
                } while (!request.isEmpty());


                String [] parti = primaRiga.split(" ");
                String path = parti[1];    

                if("/ciao.html".equals(path)){
                    String response = "Buongiorno admin!";
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: " + response.length());
                    out.println("Connection: keep-alive"); 
                    out.println();
                    out.println(response);
                } else if("/ciao".equals(path)){
                    out.println("HTTP/1.1 301 Moved Permanently");
                    out.println("Location: /ciao.html");
                    out.println("Content-Length: 0");
                    out.println("Connection: keep-alive"); 
                    out.println();
                } else{
                    String response = "404 Not Found";
                    out.println("HTTP/1.1 404 Not Found");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: " + response.length());
                    out.println("Connection: keep-alive"); 
                    out.println();
                    out.println(response);
                }

                //s.close();
            }
        }
    }
}
/* 
/ciao.html -> 200
/ciao -> 301    (header: Location .....), nuova location viene salvata per poi non dare problemi
/ * -> 404
*/