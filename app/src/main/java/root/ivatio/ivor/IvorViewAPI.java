package root.ivatio.ivor;

import root.ivatio.activity.msg.Message;
import root.ivatio.activity.msg.UserRoles;

public interface IvorViewAPI {
    void appendMessage(Message msg);
    void setRole(UserRoles role);
    void switchButtonDelete(int state);
    void showMessage(int res);
    void hideRating();
    void showRating();
    void needEval();
    void appendUserMessage(String input);
    void clearInputFild();
    void scrollListMessagesToBottom();
}
