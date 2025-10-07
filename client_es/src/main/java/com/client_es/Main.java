package com.client_es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
   public Main() {
   }

   public static void main(String[] args) throws UnknownHostException, IOException {
    System.out.println("Inserisci IP del server");
    Scanner sc = new Scanner(System.in);
    String ip = sc.nextLine();
    System.out.println("Inserisci la porta del server");
    Integer port = Integer.parseInt(sc.nextLine());

    Socket socket = new Socket(ip, port);
    System.out.println("Conessione affetuata");
      
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    
    String versione = in.readLine();
    System.out.println("Versione" + versione);
    
    do{
        System.out.println("Inserire parola da convertire");
        String myFrase = sc.nextLine();

        out.println(myFrase);

        if(myFrase.equals("!")){

            break;
        }

        String upperFrase = in.readLine();

        System.out.println(upperFrase);
    }while(true);
    
    System.out.println("Cominicazione terminata");  socket.close();
   }
}
