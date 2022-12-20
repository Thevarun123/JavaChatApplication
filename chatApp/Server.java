
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
import java.io.*;
import java.net.*;

public class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // thread-read karke deta rhega...
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("client : " + msg);

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - data user se lega and send karega client koo....
        Runnable r2 = () -> {
            System.out.println("writer started...");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        new Server();
    }
}