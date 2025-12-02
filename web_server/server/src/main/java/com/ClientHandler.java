package com;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outBinary = new DataOutputStream(socket.getOutputStream());
        ) {
            String primaRiga = in.readLine();
            if (primaRiga == null || primaRiga.isEmpty()) {
                return;
            }

            String request;
            do {
                request = in.readLine();
                if (request != null) {
                    System.out.println(request);  
                }
            } while (request != null && !request.isEmpty());

            String[] parti = primaRiga.split(" ");
            String method = parti[0];
            String path = parti[1];

            if (!path.contains(".") && !path.endsWith("/")) {
                path += "/";
                out.println("HTTP/1.1 302 Found");
                out.println("Location: " + path);
                out.println("Connection: close");
                out.println();
                return;
            }

            if (!method.equalsIgnoreCase("GET")) {
                String body = "<h1>405 - Method Not Allowed </h1>";
                out.println("HTTP/1.1 405 Method Not Allowed");
                out.println("Allow: GET");
                out.println("Content-Length: " + body.length());
                out.println();
                out.println(body);
                return;
            }

            if (!path.contains(".") && !path.endsWith("/")) {
                String correctPath = path + ".html";
                out.println("HTTP/1.1 301 Moved Permanently");
                out.println("Location: " + correctPath);
                out.println("Connection: close");
                out.println();
                return;
            }

            File file = new File("htmldox/" + path);

            if (!file.exists()) {
                String body = "<h1>404 - File Not Found </h1>";
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + body.length());
                out.println();
                out.println(body);
                return;
            }

            String contentT = getContentType(file);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentT);
            out.println("Content-Length: " + file.length());
            out.println("Connection: close");
            out.println();

            try (BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    outBinary.write(buffer, 0, bytesRead);
                }
            }

            outBinary.flush();  

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getContentType(File f) {
        String fileName = f.getName();
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";  
        }
    }
    
}
