package httpserver.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static httpserver.utilities.HTTPResponseCodes.HTTP_200_OK;

/*
 * Respond to HTTP requests and serve appropriate HTML.
 *
 * TODO: Read templates from files.
 */
public class HTTPWorker implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private String request;
    private Socket socket;

    private static final String HTML_HEAD = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"></head>\r\n";

    private static String httpTemplateOpen =
        "<!DOCTYPE html>\r\n<html lang='en'>\r\n<head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'><link href='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1' crossorigin='anonymous'></head><body><script src=https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js' integrity='sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW' crossorigin='anonymous'></script><div class='container-fluid'>";

    public HTTPWorker(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        this.request = "";
        
        try {
            this.request += "<h1>HTTP Request</h1>";
            this.request += "<ul class=\"list-group list-group-flush\">\n";
            while (this.in.ready()) {
                this.request += "<li class=\"list-group-item\">";
                this.request += in.readLine();
                this.request += "</li>\n";
            }
            this.request += "</ul>";
        }

        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        /*
         * Yes, this part is ugly.
         * TODO: Create a HTML renderer class?        
         */
        System.out.println("Handling HTTP request");
        System.out.println(this.request);
        out.println(HTTP_200_OK);
        out.println(httpTemplateOpen);
        out.println("<h1 class=\"display-1\">Hello, world!</h1>");
        out.println("<p>" + this.request + "</p>");
        out.println("</div></body></html>");
        out.println();

        /*
         * Tidy up. Might be redundant if GC takes care of this.
         *
         * socket.close() throws a (non-fatal) NullPointerException. Why?        
         */
        try {
            this.socket.close();
        }

        catch (NullPointerException e) {
            System.out.println("Failed to close the socket - already gone?");
            System.out.println("Error message: " + e.getMessage());
        }

        catch (IOException e) {
            System.out.println("Closing the socket threw a NullPointerException at us!");
            System.out.println("Error message: " + e.getMessage());
        }

        finally {
            System.out.println("HTTP worker thread finished.");
        }
    }
}
