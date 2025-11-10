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
    private String username = null;
    private static ArrayList<Message> board;
    private static int nextId = 1;

    public Main(Socket s, ArrayList<Message> board) {
        this.s = s;
        Main.board = board;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

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
                } else if (line.equalsIgnoreCase("QUIT")) {
                    out.println("BYE");
                    break;
                } else if (line.startsWith("ADD ")) {
                    String text = line.substring(4).trim();
                    if (text.isEmpty()) {
                        out.println("ERR SYNTAX");
                    } else {
                        synchronized (board) {
                            Message msg = new Message(nextId++, username, text);
                            board.add(msg);
                            out.println("OK ADDED " + msg.getId());
                        }
                    }
                } else if (line.equalsIgnoreCase("LIST")) {
                    synchronized (board) {
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
                } else if (line.startsWith("DEL ")) {
                    try {
                        int idToDelete = Integer.parseInt(line.substring(4).trim());
                        boolean found = false;
                        synchronized (board) {
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
                        }
                        if (!found) {
                            out.println("ERR NOTFOUND");
                        }
                    } catch (NumberFormatException e) {
                        out.println("ERR SYNTAX");
                    }
                } else {
                    out.println("ERR UNKNOWNCMD");
                }
            }
            System.out.println("Connessione terminata con " + username);

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