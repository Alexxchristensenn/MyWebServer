import java.io.IOException;
import java.io.*;
import java.net.*;

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


        try (ServerSocket welcomeSocket = new ServerSocket(port)) {
            while (true) {
                // Accept incoming client connection
                Socket connectionSocket = welcomeSocket.accept();

                // Set up input stream
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                // Set up output stream
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                //Here, everything is established, and we can begin passing data like HTTP requests
                String clientRequest = inFromClient.readLine();
                System.out.println("Received request: " + clientRequest);



                //Done with HTTP, ready to close
                connectionSocket.close();
            }
            /* End of While loop, loops back and waits for another client connection */
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
