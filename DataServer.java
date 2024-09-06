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
    
        // Keep accepting client connections in a loop
        while (true) {
            Socket clientSocket = serverSocket.accept();
            // For each client connection, start a new thread with ClientHandler
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    // The ClientHandler class to handle each client connection
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

                String requestLine;
                // Keep reading requests from the client
                while ((requestLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + requestLine);

                    // Split the command into parts
                    String[] requestParts = requestLine.split(" ");
                    String command = requestParts[0];
                    String key = requestParts.length > 1 ? requestParts[1] : null;
                    String value = requestParts.length > 2 ? requestParts[2] : null;

                    // Check if the command is valid (either "PUT" or "GET")
                    if (command.equalsIgnoreCase("PUT")) {
                        if (key != null && value != null) {
                            dataStore.put(key, value);
                            out.println("Data stored: " + key + " = " + value);
                        } else {
                            out.println("Invalid PUT request. Provide both key and value.");
                        }
                    } else if (command.equalsIgnoreCase("GET")) {
                        if (key != null && dataStore.containsKey(key)) {
                            out.println("Data for " + key + ": " + dataStore.get(key));
                        } else {
                            out.println("No data found for key: " + key);
                        }
                    } else {
                        // If the command is neither PUT nor GET, return "Unknown command"
                        out.println("Unknown command.");
                    }
                }

                // Close the connection after the client finishes
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
