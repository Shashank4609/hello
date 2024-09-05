import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class DataClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address
        int port = 8080; // Server port

        try {
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Example PUT requests
            out.println("PUT key2 value2");
            System.out.println("Server response: " + in.readLine());  // Read response after each PUT

            out.println("PUT key3 value3");
            System.out.println("Server response: " + in.readLine());  // Read response after each PUT

            // Example GET requests
            out.println("GET key2");
            System.out.println("Server response: " + in.readLine());  // Read response after each GET

            out.println("GET key3");
            System.out.println("Server response: " + in.readLine());  // Read response after each GET

            // Close resources after all requests have been made
            out.close();
            in.close();
            socket.close();

        } catch (SocketException e) {
            System.err.println("SocketException: Connection reset or closed: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
