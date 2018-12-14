package root.ivatio.ivor;

import root.ivatio.Message;
import root.ivatio.activity.MsgActivity;
import root.ivatio.util.ROLE;

public interface IvorViewAPI {
    void appendMessage(Message msg);
    void setRole(ROLE role);
    void switchButtonDelete(int state);
    void showMessage(int res);
    void removeRating();
    void appendRating();
    void needEval();
}
