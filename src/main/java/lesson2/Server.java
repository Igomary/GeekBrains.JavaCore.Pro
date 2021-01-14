package lesson2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Server {
    private List<Message> messages;
    private List<ClientHandler> clients;
    private AuthService authService;
    private Scanner scanner;

    private Server() {
        authService = new AuthService();
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        scanner = new Scanner(System.in);
    }


    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8021);
            System.out.println("Для добавления нового пользователя в базу данных введите /add, затем имя, логин и пароль (через пробел)");
            while (true) {
                addNewUser();
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    ClientHandler clientHandler = new ClientHandler(this, socket);
                    try {
                        clientHandler.startHandler();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public static void main(String[] args) {
        new Server().startServer();
    }

    synchronized public void addNewMsg(Message message) throws IOException {
        System.out.println(message + " /to: "+ message.getTo());
        messages.add(message);
        if (message.getTo().equals("everybody")) {
            for (ClientHandler handler : clients) {
                if (!handler.getUser().equals(message.getUser())) {
                    handler.sendMessage(message);
                }
            }
        } else {
            for (ClientHandler handler : clients) {
                if (handler.getUser().getLogin().equals(message.getTo())) {
                    handler.sendMessage(message);
                }
            }
        }
    }

    synchronized public void  addNewClient(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        addNewMsg(new Message(clientHandler.getUser(), "присоеднинился к чату"));
    }

    synchronized void disconnectUser(ClientHandler clientHandler) throws IOException {
        if (clients.contains(clientHandler)) {
            clients.remove(clientHandler);
            addNewMsg(new Message(clientHandler.getUser(),"покинул чат"));
        }
    }

    public List<String> getConnectedUsersLogin() {
        List<String> logins = new ArrayList<>();
        for (ClientHandler clientHandler : clients) {
            logins.add(clientHandler.getUser().getLogin());
        }
        return logins;
    }

    private void addNewUser() {
        String string = scanner.nextLine();
        String[] cmd = string.split("\\s");
        if (cmd[0].equals("/add")) {
            String name = cmd[1];
            String login = cmd[2];
            String password = cmd[3];
            this.authService.addUserToDb(name, login, password);
        }
    }
}
