package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import java.util.List;

import io.reactivex.functions.Consumer;

public class ActionSendGPS extends Action {
    public ActionSendGPS(String cmd, Consumer<String> next) {
        super(cmd, next);
        buffer = new CircularArray<>();
        buffer.addFirst(endMessage);
    }

    @Override
    public void put(String param) {
    }

    @Override
    public List<String> getParam() {
        return null;
    }
}
