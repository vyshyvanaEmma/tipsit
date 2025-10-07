package com.server_es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//Server
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Server socket in ascolto sulla porta 3000");

        ServerSocket ss = new ServerSocket(3000);   //apro server socket sulla porta 3000
        Socket myS = ss.accept();                        //crea un socket in ascolta da server, accept blocca, finche qualcuno non si collega alla porta 3000
        System.out.println("Qualuno si è collegato");

        BufferedReader in = new BufferedReader(new InputStreamReader(myS.getInputStream()));
        PrintWriter out = new PrintWriter(myS.getOutputStream(), true);
        
        
        out.println("Welcome to server v2.1");

        boolean check = true;
        do {
            String fraseReceived = in.readLine();
            if (fraseReceived == null) {
                check = false;
                break;
            }
            
            fraseReceived = fraseReceived.toUpperCase();
        
            if(fraseReceived.equals("!")){
                check = false;
                break;
            }
            out.println(fraseReceived);
        
        } while (check);
        
        System.out.println("Communicazione terminata");
        
        ss.close();                                      //chiudo la porta
        
    }
}

//10.22.9.6