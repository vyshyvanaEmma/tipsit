package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Inserisci IP del server:");
            String ip = sc.nextLine();

            System.out.println("Inserisci la porta del server:");
            int port = Integer.parseInt(sc.nextLine());

            try (Socket socket = new Socket(ip, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connessione effettuata con " + ip + ":" + port);

                String versione = in.readLine();
                System.out.println("Versione: " + versione);

                while (true) {
                    System.out.println("Inserire parola da convertire (exit per terminare):");
                    String myFrase = sc.nextLine();

                    if (myFrase.equalsIgnoreCase("exit")) {
                        out.println("!");
                        break;
                    }

                    out.println(myFrase);
                    String upperFrase = in.readLine();
                    System.out.println("Risposta: " + upperFrase);
                }

                System.out.println("Comunicazione terminata");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
