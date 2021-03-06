package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private final Logger logger = Logger.getLogger(Server.class.getName());

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);
        Handler handler = null;
        try {
            handler = new FileHandler("log_main.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.setLevel(Level.INFO);
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                StringBuilder sb = new StringBuilder(logRecord.getSourceMethodName()+" " + logRecord.getResourceBundleName() + System.lineSeparator() + logRecord.getLoggerName() + " " + logRecord.getLevel() + ": " + logRecord.getMessage());
                return sb.toString();
            }});
        logger.addHandler(handler);

        clients = new Vector<>();
        authService = new SimpleAuthService();
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(8189);
             DBHelper instance = DBHelper.getInstance()) {

            System.out.println("Сервер запущен на порту 8189");
            logger.log(Level.INFO,"Сервер запущен на порту 8189" );
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket, authService, executorService);
                System.out.println("Подключился новый клиент");
                logger.log(Level.INFO, "Подключился новый клиент");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        System.out.println("Сервер завершил свою работу");
        logger.log(Level.INFO, "Cервер завершил свою работу");
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public void privateMsg(ClientHandler sender, String receiverNick, String msg) {
        if (sender.getNickname().equals(receiverNick)) {
            sender.sendMsg("заметка для себя: " + msg);
            return;
        }
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(receiverNick)) {
                o.sendMsg("от " + sender.getNickname() + ": " + msg);
                sender.sendMsg("для " + receiverNick + ": " + msg);
                return;
            }
        }
        sender.sendMsg("Клиент " + receiverNick + " не найден");
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getNickname()).append(" ");
        }
        sb.setLength(sb.length() - 1);
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }
}
