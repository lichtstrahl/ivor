package root.ivatio;

import java.util.Date;

import Users.User;

public class Message {
    public User author;
    public String content;
    public Date time;

    public Message(User user, String content, Date time) {
        this.author = user;
        this.content = content;
        this.time = time;
    }
}
