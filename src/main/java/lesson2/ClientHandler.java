package lesson2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private final int maxTryCount = 5;
    private User user;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void startHandler() throws IOException {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            if (!authorization()) {
                close();
                return;
            }
            server.addNewClient(this);
            msgListener();
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
    }

    synchronized public void sendMessage(Message message) throws IOException {
        dataOutputStream.writeUTF(message.toString());
    }

    private boolean authorization() throws IOException {
        int tryCount = 0;
        while (true) {
            dataOutputStream.writeUTF("Введите для авторизации /auth, после - логин и пароль через пробел (/exit - выход)");
            String message = dataInputStream.readUTF();
            String[] messages = message.split("\\s");
            if (messages.length == 3 && messages[0].equals("/auth")&& tryCount<maxTryCount) {
                tryCount++;
                user = server.getAuthService().signIn(messages[1], messages[2]);
                if (user != null) {
                    return true;
                } else {
                    dataOutputStream.writeUTF("Неверный логин и/или пароль");
                }
            } else if (tryCount >= maxTryCount) {
                dataOutputStream.writeUTF("Исчерпан лимит попыток авторизации");
                close();
                return false;
            } else if (messages[0].equals("/exit")) {
                close();
                return false;
            }
        }
    }

    private void msgListener() throws IOException {
        while (true) {
            String message = dataInputStream.readUTF();
            String[] messages = message.split("\\s");
            if (message.equals("/exit")) {
                close();
            } else if (messages[0].equals("/w")) {
                String to = messages[1];
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < messages.length; i++) {
                    sb.append(messages[i]);
                }
                String msg = sb.toString();
                server.addNewMsg(new Message(user,msg,to));
            } else{
                server.addNewMsg(new Message(user, message));
            }
        }
    }

    public User getUser() {
        return user;
    }

    private void close() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
        server.disconnectUser(this);
    }

}
