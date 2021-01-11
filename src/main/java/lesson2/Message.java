package lesson2;

public class Message {
    private User user;
    private String message;
    private String to;

    public Message(User user, String message) {
        this.user = user;
        this.message = message;
        this.to = "everybody";
    }

    public Message (User user, String message, String to) {
        this.user = user;
        this.message = message;
        this.to = to;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return user.getName()+": "+ message;
    }
}
