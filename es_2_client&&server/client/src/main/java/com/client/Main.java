// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.client;

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
      Integer port = sc.nextInt();
      Socket socket = new Socket(ip, port);
      System.out.println("Conessione affetuata");
      new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      out.println("Ciao rossi");
      socket.close();
   }
}
