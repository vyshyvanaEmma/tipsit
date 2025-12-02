package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static int PORT = 3000;
    public static void main(String[] args) throws IOException {
        while(true){
            try (ServerSocket ss = new ServerSocket(PORT)) {
                Socket s = ss.accept();

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String primaRiga  = in.readLine();
                String request;
                do {
                    request = in.readLine();
                    System.out.println(request);
                } while (!request.isEmpty());


                String [] parti = primaRiga.split(" ");
                String path = parti[1];    

                if("/ciao.html".equals(path)){
                    String response = "Buongiorno admin!";
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: " + response.length());
                    out.println("Connection: keep-alive"); 
                    out.println();
                    out.println(response);
                } else if("/ciao".equals(path)){
                    out.println("HTTP/1.1 301 Moved Permanently");
                    out.println("Location: /ciao.html");
                    out.println("Content-Length: 0");
                    out.println("Connection: keep-alive"); 
                    out.println();
                } else{
                    String response = "404 Not Found";
                    out.println("HTTP/1.1 404 Not Found");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: " + response.length());
                    out.println("Connection: keep-alive"); 
                    out.println();
                    out.println(response);
                }

                //s.close();
            }
        }
    }
}
/* 
/ciao.html -> 200
/ciao -> 301    (header: Location .....), nuova location viene salvata per poi non dare problemi
/ * -> 404
*/



/* 
curl -X GET http://localhost:3000 \
  -H "Host: localhost:3000" \
  -H "Connection: keep-alive" \
  -H "Upgrade-Insecure-Requests: 1" \
  -H "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36" \
  -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*;q=0.8,application/signed-exchange;v=b3;q=0.7" \
  -H "Sec-Fetch-Site: none" \
  -H "Sec-Fetch-Mode: navigate" \
  -H "Sec-Fetch-User: ?1" \
  -H "Sec-Fetch-Dest: document" \
  -H "sec-ch-ua: \"Google Chrome\";v=\"117\", \"Not;A=Brand\";v=\"8\", \"Chromium\";v=\"117\"" \
  -H "sec-ch-ua-mobile: ?0" \
  -H "sec-ch-ua-platform: \"Linux\"" \
  -H "Accept-Encoding: gzip, deflate, br" \
  -H "Accept-Language: en-US,en;q=0.9"
*/