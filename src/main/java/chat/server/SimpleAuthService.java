package chat.server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private final DBHelper dbHelper = DBHelper.getInstance();

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return dbHelper.findByLoginAndPassword(login, password);
    }

    @Override
    public boolean updateNickname(String oldName, String newName) {
        int result = dbHelper.updateName(oldName, newName);
        return result == 1;
    }


}
