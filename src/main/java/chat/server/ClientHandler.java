package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientHandler {
    private String nickname;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public String getNickname() {
        return nickname;
    }

    public ClientHandler(Server server, Socket socket, AuthService authService, ExecutorService executorService) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            executorService.submit(() -> {
                try {
                    waitAuthorization(server);
                    waitMessageOrCommand(server, authService);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClientHandler.this.disconnect();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitAuthorization(Server server) throws IOException {
        while (true) {
            String msg = in.readUTF();

            if (msg.startsWith("/auth")) {
                System.out.println("MSG: "+ msg);
                String[] tokens = msg.split("\\s");
                String nick = server.getAuthService().getNicknameByLoginAndPassword(tokens[1], tokens[2]);

                if (nick != null && !server.isNickBusy(nick)) {
                    sendMsg("/authok " + nick);
                    nickname = nick;
                   // System.out.println("/authok" + nick);
                    server.subscribe(this);
                    break;
                }
            }
        }
    }

    private void waitMessageOrCommand(Server server, AuthService authService) throws IOException {
        while (true) {
            String msg = in.readUTF();

            if (msg.startsWith("/")) {
                if (msg.equals("/end")) {
                    sendMsg("/end");
                    break;
                }

                checkPrivateMessageCommand(server, msg);
                checkUpdateMessageCommand(server, authService, msg);
            } else{
                server.broadcastMsg(nickname+": " + msg);
            }

        }
    }

    private void checkPrivateMessageCommand(Server server, String message) {
        if (message.startsWith("/w ")) {
            String[] tokens = message.split("\\s", 3);
            server.privateMsg(this, tokens[1], tokens[2]);
        }
    }

    private void checkUpdateMessageCommand(Server server,AuthService authService, String message) {
        if (message.startsWith("/upNick ")) {
            String[] tokens = message.split("\\s", 2);

            if (tokens.length == 2 && !tokens[1].trim().equals(" ")) {
                String newNickName = tokens[1].trim();

                if (!server.isNickBusy(newNickName) && authService.updateNickname(getNickname(), newNickName)) {
                    String msg = String.format("Имя пользователя \"%s\" был заменен на \"%s\"", getNickname(), newNickName);
                    server.broadcastMsg(message);

                    nickname = newNickName;
                    server.broadcastClientsList();
                }
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        server.unsubscribe(this);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
