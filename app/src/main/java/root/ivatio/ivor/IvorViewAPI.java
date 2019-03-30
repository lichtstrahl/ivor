package root.ivatio.ivor;

import javax.annotation.Nullable;

import root.ivatio.Message;
import root.ivatio.util.ROLE;

public interface IvorViewAPI {
    void appendMessage(@Nullable Message msg);
    void setRole(ROLE role);
    void switchButtonDelete(int state);
    void showMessage(int res);
    void removeRating();
    void appendRating();
    void needEval();
    void switchProgress(int state);
    void inputEnabled(boolean flag);
}
