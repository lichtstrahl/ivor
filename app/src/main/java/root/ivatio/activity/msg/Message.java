package root.ivatio.activity.msg;

import java.util.Calendar;
import java.util.Date;

import root.ivatio.bd.users.User;

public class Message {
    public User author;
    public String content;
    public Date time;

    public Message(User user, String content, Date time) {
        this.author = user;
        this.content = content;
        this.time = time;
    }

    public Message(User user, String content) {
        this.author = user;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

    public static Message getUserMessage(User user, String content) {
        return new Message(user, content);
    }

    public static Message getIvorMessage(String content) {
        return new Message(null, content);
    }
}
