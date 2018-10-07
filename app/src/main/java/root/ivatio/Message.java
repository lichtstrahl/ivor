package root.ivatio;

import java.util.Calendar;
import java.util.Date;

import bd.users.User;

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
}
