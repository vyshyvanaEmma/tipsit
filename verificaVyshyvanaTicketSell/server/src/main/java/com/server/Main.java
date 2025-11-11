package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Thread {
    private Socket s;
    private Ticket ticket; 
    private PrintWriter out;
    private BufferedReader in;
    private String username = null;
    boolean logedIn = false;

    private static ArrayList<Main> clienti = new ArrayList<>();


    public Main(Socket s, Ticket ticket) {
        this.s = s;
        this.ticket = ticket;
    }
    
    private static void notificaClienti(String s){
        
        for (Main cliente : clienti){
            if(cliente.out != null){
                cliente.out.println(s);
            }
        }

    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            synchronized(clienti){
                clienti.add(this);
            }

            out.println("WELCOME");
            String line;
            while ((line = in.readLine()) != null) {
                String[] parti = line.split(" ");
                String testo = parti[0].toUpperCase();
            
                if (testo.equals("LOGIN")) {
                    if(parti.length > 1){
                        username = parti[1];
                    }else{
                        username = null;
                    }

                    if(username == null || username.isEmpty()){
                        out.println("ERR LOGINREQUIRED");
                    } else{
                        logedIn = true;
                        out.println("OK");
                    }
                } else if(!logedIn){
                    out.println("ERR LOGINREQUIRED");
                } else if (testo.equalsIgnoreCase("N")) {
                    synchronized (ticket) {
                        out.println("AVAIL Gold: " + ticket.getQuantitaG() + " Pit: " + ticket.getQuantitaPit() + " Parterre: " + ticket.getQuantitaPar());
                        out.println("END");
                    }
                } else if (testo.equals("QUIT")) {
                    out.println("BYE");
                    break;
                } else if (testo.equals("BUY")) {
                    if(parti.length != 3){
                        out.println("ERR SYNTAX");
                    }
                    String tipo = parti[1];
                    int quantita = 0;

                    try{
                        quantita = Integer.parseInt(parti[2]);
                    } catch(NumberFormatException e){
                        out.println("ERR SYNTAX");
                    }
                    synchronized (ticket){
                        if(tipo.equalsIgnoreCase("GOLD")){
                            if(ticket.getQuantitaG() >= quantita){
                                ticket.setQuantitaG(ticket.getQuantitaG() - quantita);
                                if(ticket.getQuantitaG() <= 0){
                                    notificaClienti("NOTIFY SOLD OUT Gold");
                                }
                                out.println("OK"); 
                            }else{
                                out.println("KO");
                            }
                        } else if(tipo.equalsIgnoreCase("Pit")){
                            if(ticket.getQuantitaPit() >= quantita){
                                ticket.setQuantitaPit(ticket.getQuantitaPit() - quantita);
                                if(ticket.getQuantitaPit() <= 0){
                                    notificaClienti("NOTIFY SOLD OUT Pit");
                                }
                                out.println("OK"); 
                            }else{
                                out.println("KO");
                            }
                        } else if(tipo.equalsIgnoreCase("Parterre")){
                            if(ticket.getQuantitaPar() >= quantita){
                                ticket.setQuantitaPar(ticket.getQuantitaPar() - quantita);
                                if(ticket.getQuantitaPar() <= 0){
                                    notificaClienti("NOTIFY SOLD OUT Parterre");
                                }
                                out.println("OK"); 
                            }else{
                                out.println("KO");
                            }
                        } else{
                            out.println("ERR UNKNOWNTYPE");
                        }
                    }

                } else{
                    out.println("ERR SYNTAX");
                }
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                if (s != null)
                    s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connessione terminata con " + username);
        }
    }
}