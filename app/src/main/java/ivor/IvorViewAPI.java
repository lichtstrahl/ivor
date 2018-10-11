package ivor;

import root.ivatio.Message;
import root.ivatio.activity.MsgActivity;

public interface IvorViewAPI {
    void appendMessage(Message msg);
    void setRole(MsgActivity.ROLE role);
    void switchButtonDelete(int state);
    void showMessage(int res);
    void removeRating();
    void appendRating();
    void needEval();
}
