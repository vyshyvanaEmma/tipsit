package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Inserisci l'indirizzo IP del server: ");
        String serverIP = sc.nextLine();

        System.out.println("Inserisci la porta del server: ");
        int port = sc.nextInt();
        sc.nextLine();

        try {
            Socket s = new Socket(serverIP, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            System.out.println("Connessione stabilita con il server");

            System.out.println(in.readLine());

            String input;
            while (true) {
                System.out.println("Scrivi un commando (QUIT per uscire)");
                input = sc.nextLine();

                out.println(input);

                String res;
                while ((res = in.readLine()) != null) {
                    System.out.println(res);
                    if (res.equals("END") || res.equals("BYE") || res.startsWith("ERR") || res.startsWith("OK")) {
                        break;
                    }
                }

                if (input.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Errore di conessione");
            e.printStackTrace();
        }

        sc.close();
    }
}

// 192.168.56.1