package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSingleClient {
    public static int PORT = 3000;
    private static ArrayList<Message> board = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta: " + PORT);

            while (true) {
                System.out.println("In attesa di un client...");
                try (Socket s = ss.accept();
                     PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

                    System.out.println("Client collegato da: " + s.getInetAddress().getHostAddress());
                    String username = null;
                    out.println("WELCOME");

                    String line;
                    while ((line = in.readLine()) != null) {

                        if (username == null) {
                            if (line.startsWith("LOGIN ")) {
                                username = line.substring(6).trim();
                                if (!username.isEmpty()) {
                                    out.println("OK");
                                } else {
                                    out.println("ERR LOGINREQUIRED");
                                    username = null;
                                }
                            } else {
                                out.println("ERR LOGINREQUIRED");
                            }
                        }
                        else if (line.equalsIgnoreCase("QUIT")) {
                            out.println("BYE");
                            break;
                        }
                        else if (line.startsWith("ADD ")) {
                            String text = line.substring(4).trim();
                            if (text.isEmpty()) {
                                out.println("ERR SYNTAX");
                            } else {
                                Message msg = new Message(nextId++, username, text);
                                board.add(msg);
                                out.println("OK ADDED " + msg.getId());
                            }
                        }
                        else if (line.equalsIgnoreCase("LIST")) {
                            if (board.isEmpty()) {
                                out.println("BOARD: vuota. END");
                            } else {
                                out.println("BOARD:");
                                for (Message msg : board) {
                                    out.println(msg.toString());
                                }
                                out.println("END");
                            }
                        }
                        else if (line.startsWith("DEL ")) {
                            try {
                                int idToDelete = Integer.parseInt(line.substring(4).trim());
                                boolean found = false;
                                for (Message msg : new ArrayList<>(board)) {
                                    if (msg.getId() == idToDelete) {
                                        found = true;
                                        if (msg.getAuthor().equals(username)) {
                                            board.remove(msg);
                                            out.println("OK DELETED " + idToDelete);
                                        } else {
                                            out.println("ERR PERMISSION");
                                        }
                                        break;
                                    }
                                }
                                if (!found) {
                                    out.println("ERR NOTFOUND");
                                }
                            } catch (NumberFormatException e) {
                                out.println("ERR SYNTAX");
                            }
                        }
                        else {
                            out.println("ERR UNKNOWNCMD");
                        }
                    }

                    System.out.println("Client " + username + " disconnesso");

                } catch (IOException e) {
                    System.out.println("Errore con il client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
