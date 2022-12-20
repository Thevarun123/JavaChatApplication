import java.net.*;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Times New Roman", Font.PLAIN, 20);

    // constructor
    public Client(String ip, int port) {

        try {
            System.out.println("Sending request to server");
            socket = new Socket(ip, port);
            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("key release" + e.getKeyCode());
                if (e.getKeyChar() == 10) {
                    System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    out.println(contentToSend);
                    out.flush();
                    messageArea.append("Me: " + contentToSend + "\n");
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }

        });
    }

    public void createGUI() {
        // gui code

        this.setTitle("Client Messenger[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        // ImageIcon icon = new ImageIcon(new ImageIcon("client.png"));
        // Image scaleImage = icon.getImage().getScaledInstance(10, 12,
        // Image.SCALE_DEFAULT);

        // ImageIcon icon = new ImageIcon(
        // new ImageIcon("client.png").getImage().getScaledInstance(35, 30,
        // Image.SCALE_SMOOTH));
        // heading.setIcon(icon);
        // heading.setHorizontalTextPosition(SwingConstants.CENTER);
        // heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEditable(false);

        // frame ka layout set karenge
        this.setLayout(new BorderLayout());

        // adding the component to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

    }

    public void startReading() {
        // thread-read karke deta rhega...
        Runnable r1 = () -> {
            System.out.println("reader started...");

            try {
                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageArea.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server : " + msg);
                    messageArea.append("Server: " + msg + "\n");
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
                System.out.println("connection is closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) throws Exception {
        // Scanner scn = new Scanner(System.in);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("This is client");
        System.out.println("Enter the IP address of the server");
        String ip = br2.readLine();
        System.out.println("Enter the port number");
        int port = Integer.parseInt(br2.readLine());

        new Client(ip, port);
    }
}
