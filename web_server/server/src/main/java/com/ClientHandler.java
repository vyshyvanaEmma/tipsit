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
            // primaRiga - GET /index.html HTTP/1.1 (metodo, path e versione)
            String primaRiga = in.readLine();
            if (primaRiga == null || primaRiga.isEmpty()) {
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Length: 0");
                out.println();
                logRequest(socket, "GET", "/", 400, "Unknown", 0); // Log error 400
                return;
            }

            String[] parti = primaRiga.split(" ");
            if (parti.length != 3 || !parti[2].startsWith("HTTP/")) {
                out.println("HTTP/1.1 400 Bad Request");
                out.println("Content-Length: 0");
                out.println();
                logRequest(socket, "GET", "/", 400, "Unknown", 0); // Log error 400
                return;
            }

            String method = parti[0];
            String path = parti[1];

            // user-agent
            String userAgent = "";
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("User-Agent:")) {
                    userAgent = line.split(": ", 2)[1];
                }
            }

            // redirect case (se path termina con "/")
            if (!path.contains(".") && !path.endsWith("/")) {
                path += "/";
                out.println("HTTP/1.1 301 Moved Permanently");
                out.println("Location: " + path);
                out.println("Connection: close");
                out.println();
                logRequest(socket, method, path, 301, userAgent, 0); // Log redirect
                return;
            }

            // Metodi
            int status = 200;
            int bytesSent = 0;
            switch (method.toUpperCase()) {
                case "GET":
                    File file = new File("htmldox" + path);
                    if (!file.exists()) {
                        out.println("HTTP/1.1 404 Not Found");
                        out.println("Content-Length: 0");
                        out.println();
                        status = 404;
                    } else {
                        String contentType = getContentType(file);
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: " + contentType);
                        out.println("Content-Length: " + file.length());
                        out.println();

                        // invia file bin (img, altro)
                        try (InputStream fileIn = new FileInputStream(file)) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = fileIn.read(buffer)) != -1) {
                                outBinary.write(buffer, 0, bytesRead);
                            }
                            bytesSent = (int) file.length();
                        }
                    }
                    break;

                case "POST":
                    // POST
                    String contentLengthHeader = in.readLine();
                    int contentLength = Integer.parseInt(contentLengthHeader.split(": ")[1]);
                    String body = readBody(in, contentLength);

                    String responseBody = "<html><body><h1>Echo della richiesta</h1><pre>" + body + "</pre></body></html>";
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("Content-Length: " + responseBody.length());
                    out.println();
                    out.println(responseBody);
                    status = 200;
                    bytesSent = responseBody.length();
                    break;

                case "HEAD":
                    // HEAD
                    File fileHead = new File("htmldox" + path);
                    if (!fileHead.exists()) {
                        out.println("HTTP/1.1 404 Not Found");
                        out.println("Content-Length: 0");
                        out.println();
                        status = 404;
                    } else {
                        String contentTypeHead = getContentType(fileHead);
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: " + contentTypeHead);
                        out.println("Content-Length: " + fileHead.length());
                        out.println();
                        bytesSent = 0; 
                    }
                    break;

                default:
                    out.println("HTTP/1.1 405 Method Not Allowed");
                    out.println("Content-Length: 0");
                    out.println();
                    status = 405;
                    break;
            }

            // log req
            logRequest(socket, method, path, status, userAgent, bytesSent);

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

    // logging
    private void logRequest(Socket socket, String method, String path, int status, String userAgent, int bytesSent) {
        String clientIP = socket.getInetAddress().getHostAddress();
        String logLine = String.format("%s - %s %s %d \"%s\" %d", clientIP, method, path, status, userAgent, bytesSent);

        try (FileWriter fw = new FileWriter("access.log", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(logLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lettura del body per POST
    private static String readBody(BufferedReader in, int contentLength) throws IOException {
        if (contentLength <= 0) {
            return "";
        }
        char[] buf = new char[contentLength];
        int read = 0;
        while (read < contentLength) {
            int n = in.read(buf, read, contentLength - read);
            if (n == -1) {
                break;
            }
            read += n;
        }
        return new String(buf, 0, read);
    }

    // Gestione del Content-Type
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
