import java.io.IOException;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

class MyWebServer {
    public static void main(String[] args) throws IOException {
        //Check there are 2 arguments
        if (args.length != 2) {
            System.err.println("Usage: java MyWebServer <port_number> <evaluationWeb_directory>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String evaluationWeb = args[1];

        System.out.println("port = " + port);
        System.out.println("evaluationWeb = " + evaluationWeb);


        try (ServerSocket welcomeSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));) {
            while (true) {
                System.out.println("Server is listening on " + welcomeSocket.getInetAddress().getHostAddress() + ":" + port);

                // Accept incoming client connection
                Socket connectionSocket = welcomeSocket.accept();

                // Set up input stream
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                //Read HTTP request
                String requestLine = inFromClient.readLine();
                System.out.println("requestLine = " + requestLine);

                String headerLine;
                Map<String, String> headers = new HashMap<>();
                while ((headerLine = inFromClient.readLine()) != null && !headerLine.isEmpty()) {
                    String[] headerParts = headerLine.split(": ", 2);
                    if (headerParts.length == 2) {
                        headers.put(headerParts[0], headerParts[1]);
                    }
                }

                HTTPRequestObject httpRequest = new HTTPRequestObject(requestLine, headers, evaluationWeb);
                System.out.println("Command: = " + httpRequest.getCommand());
                System.out.println("Path: = " + httpRequest.getPath());
                System.out.println("If-Modified-Since: = " + httpRequest.getIfModifiedSince());

                // Set up output stream
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());



                //Done with HTTP, ready to close
                //connectionSocket.close();
            }
            /* End of While loop, loops back and waits for another client connection */
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Server has stopped.");
    }

}
