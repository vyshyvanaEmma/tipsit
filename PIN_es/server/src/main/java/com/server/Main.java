package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Thread {
    private Socket s;
    private PrintWriter out;
    private BufferedReader in;

    private static ArrayList<Main> clienti = new ArrayList<>();

    public Main(Socket s) {
        this.s = s;
    }

    private static int pin = 54321;
    private static boolean[] zone = {false, false, false, false}; // ABCD

    @Override
    public void run() {
        try {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            synchronized (clienti) {
                clienti.add(this);
            }

            String line;
            while ((line = in.readLine()) != null) {
                String[] parti = line.split(" ");
                String testo = parti[0];

                if (testo.matches("\\d+")) {
                    if (!(Integer.parseInt(testo) == pin)) {
                        out.println("ERR PIN");
                    } else {
                        String cmd = parti[1];

                        if (cmd.equals("g")) {
                            if (parti.length < 3) {
                                out.println("ERR SYNTAX");
                                continue;
                            }
                            String zona = parti[2];
                            int zonaI = -1;

                            switch (zona) {
                                case "A":
                                    zonaI = 0;
                                    break;
                                case "B":
                                    zonaI = 1;
                                    break;
                                case "C":
                                    zonaI = 2;
                                    break;
                                case "D":
                                    zonaI = 3;
                                    break;
                                default:
                                    out.println("ERR ZONE");
                                    continue;
                            }

                            if (!zone[zonaI]) {
                                zone[zonaI] = true;
                                out.println("ON");
                            } else {
                                out.println("ALREADY");
                            }

                        } else if (cmd.equals("DIS")) { 
                            for (int i = 0; i < zone.length; i++) {
                                zone[i] = false;
                            }
                            out.println("OFF ALL");

                        } else if (cmd.equals("STATUS")) { 
                            StringBuilder stato = new StringBuilder();
                            for (int i = 0; i < zone.length; i++) {
                                String statoZona = "";
                                if(zone[i]){
                                    statoZona = "ON";
                                } else{
                                    statoZona = "OFF";
                                }
                                String lettera = "";
                                switch (i) {
                                    case 0:
                                        lettera = "A";
                                        break;
                                    case 1:
                                        lettera = "B";
                                        break;
                                    case 2:
                                        lettera = "C";
                                        break;
                                    case 3:
                                        lettera = "D";
                                        break;
                                
                                    default:
                                        break;
                                }
                                stato.append(lettera) 
                                      .append(": ")
                                      .append(statoZona)
                                      .append("\n");
                            }
                            out.println(stato.toString());  
                        } else if(cmd.equals("SETPIN")){
                            String newPin = parti[2];

                            if (newPin.matches("\\d+")) {
                                if ((Integer.parseInt(newPin) == pin)) {
                                    out.println("ERR PINESISTENTE");
                                }
                                pin = Integer.parseInt(newPin);
                                out.println("SUCCESS PIN CHANGED");
                            } else{
                                out.println("ERROR PIN SET");
                            }
                        } 
                        else {
                            out.println("ERR CMD");
                        }
                    }

                } else {
                    out.println("ERR SYNTAX");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connessione terminata");
        }
    }
}
