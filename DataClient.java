import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class DataClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address
        int port = 8080; // Server port
        
         try {
            // Establish connection to server
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Example of valid PUT request
            out.println("P key2 value2");
            System.out.println("Server response: " + in.readLine());

            // Example of invalid command
            out.println("PUT key3 value3");  // Mistyped command
            System.out.println("Server response: " + in.readLine());

            // Example of valid GET request
            out.println("GET key2");
            System.out.println("Server response: " + in.readLine());

            // Properly close resources
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
