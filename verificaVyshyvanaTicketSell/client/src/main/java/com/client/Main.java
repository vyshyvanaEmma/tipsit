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
                    if(res.startsWith("LOGIN ")){
                        System.out.println("Sei stato loggatto con successo");
                        System.out.println("Ora puoi eseguire vari commandi:\n - N per vedere la lista dei biglietti disponibili; \n - BUY <tipo di biglitto> <quantita> per acqiustare una certa tipologia di biglietto (GOLD, PIT, PARRETE) di una certa quantita");
                    }
                    if(res.equals("WELCOME")){
                        System.out.println("Prima di tutto esegui login con il commando LOGIN <username>");
                    }
                    System.out.println(res);
                    if (res.equals("END") || res.equals("BYE") || res.startsWith("ERR") || res.startsWith("OK") || res.startsWith("KO")) {
                        break;
                    }
                }

                if (input.equalsIgnoreCase("QUIT")) {
                    s.close();
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
