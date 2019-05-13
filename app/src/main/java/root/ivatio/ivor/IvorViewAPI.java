package root.ivatio.ivor;

import root.ivatio.activity.msg.Message;
import root.ivatio.activity.msg.UserRoles;

public interface IvorViewAPI {
    void appendMessage(Message msg);
    void hideRating();
    void showRating();
    void appendUserMessage(String input);
    void clearInputFild();
    void scrollListMessagesToBottom();
}
