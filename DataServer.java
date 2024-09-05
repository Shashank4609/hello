import java.io.*;
import java.net.*;
import java.util.HashMap;

public class DataServer {
    // In-memory storage to simulate a database
    private static HashMap<String, String> dataStore = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int port = 8080; // Port to listen on
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port " + port);

        while (true) {
    try {
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String input;
        while ((input = in.readLine()) != null) {
            System.out.println("Received from client: " + input);
            out.println("Acknowledged: " + input); // Respond to the client
        }

        // Closing the streams and socket after finishing all client communication
        in.close();
        out.close();
        clientSocket.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    }

    // Class to handle clnt connections
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String requestLine = in.readLine(); // Read the client request
                if (requestLine != null) {
                    System.out.println("Request: " + requestLine);

                    String[] requestParts = requestLine.split(" ");
                    String command = requestParts[0];
                    String key = requestParts.length > 1 ? requestParts[1] : null;
                    String value = requestParts.length > 2 ? requestParts[2] : null;

                    // Handle different types of requests
                    if (command.equalsIgnoreCase("PUT")) {
                        // Store data on the server
                        if (key != null && value != null) {
                            dataStore.put(key, value);
                            out.println("Data stored: " + key + " = " + value);
                        } else {
                            out.println("Invalid PUT request. Provide both key and value.");
                        }
                    } else if (command.equalsIgnoreCase("GET")) {
                        // Retrieve data from the server
                        if (key != null && dataStore.containsKey(key)) {
                            out.println("Data for " + key + ": " + dataStore.get(key));
                        } else {
                            out.println("No data found for key: " + key);
                        }
                    } else {
                        out.println("Unknown command.");
                    }
                }

                // Close the connection
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
      