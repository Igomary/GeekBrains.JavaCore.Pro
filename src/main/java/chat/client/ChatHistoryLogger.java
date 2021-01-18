package chat.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatHistoryLogger {
    private static String HOME = System.getProperty("user.home");
    private static final int size = 100;
    private Path historyPath;

    public ChatHistoryLogger (String userLogin) {
        historyPath = Paths.get(HOME, "history", "history_" + userLogin + ".txt");

        if (!Files.exists(historyPath.getParent())) {
            try {
                Files.createDirectory(historyPath.getParent());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        if (!Files.exists(historyPath)) {
            try {
                Files.createFile(historyPath);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean writeToFile(String message) {
        try {
            Files.writeString(historyPath, message, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String readFromFile() {
        List<String> chatList;

        try(Stream<String> chat = Files.lines(historyPath, StandardCharsets.UTF_8)) {
            chatList = chat.collect(Collectors.toList());
            long chatSize = chatList.size();
            if (chatSize == 0) {
                return "";
            }
            System.out.println("в файле истории "+chatSize+" строк");
            long skipSize = chatSize > 100 ? (chatSize - size) : 0;

            /*long startPoint = chatSize > 100 ? (chatSize - size - 1) : 0 ;
            StringBuilder sb = new StringBuilder();
            for (int i = (int) startPoint; i < chatList.size(); i++) {
                sb.append(chatList.get(i));
                sb.append(System.lineSeparator());
            }
            return sb.toString();*/

            String result = chatList.stream()
                            .skip(skipSize)
                            .collect(Collectors.joining(System.lineSeparator()));

            return result + System.lineSeparator();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;

    }
}
