package lesson2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Client() {

    }

    private void startClient() {
        try {
            Socket socket = new Socket("localhost", 8021);
            dataInputStream = new DataInputStream(socket.getInputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String message = dataInputStream.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }).start();
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            new Thread(() -> {
                try {
                    while (true) {
                        String message = scanner.nextLine();
                        dataOutputStream.writeUTF(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Client().startClient();
    }
}
