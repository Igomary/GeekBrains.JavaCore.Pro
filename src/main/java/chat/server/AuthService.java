package chat.server;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);

    boolean updateNickname(String oldName, String newName);
}
