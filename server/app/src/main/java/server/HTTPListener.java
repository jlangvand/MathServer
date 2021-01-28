package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPListener extends Thread {
    private int port = 8080;

    private static String HTTP_OK =
        "HTTP/1.0 200 OK\r\n\r\n";

    private static String HTTP_HEADER =
        "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"></head>";

    private static String httpTemplateOpen =
        "<!DOCTYPE html>\r\n" +
        "  <html lang='en'>\r\n" +
        "    <head><meta charset='utf-8'>" +
        "          <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1' crossorigin='anonymous'>" +
                    "    </head>";


    public HTTPListener() {
    };

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader in = new BufferedReader(isr)) {
            System.out.println("Client connected");
            String inputLine, request;
            inputLine = in.readLine();
            for (request = inputLine; !inputLine.isBlank(); inputLine = in.readLine())
                request += inputLine;
            System.out.println(request);
            out.println(HTTP_OK);
            out.println(httpTemplateOpen);
            out.println("<body>");
            out.println("<h1>Hello, world</h1>");
            out.println("</body></html>");
        } catch (IOException ex) {
            System.out.println("IO Exception: " + ex.getMessage());
        }
    }
}
