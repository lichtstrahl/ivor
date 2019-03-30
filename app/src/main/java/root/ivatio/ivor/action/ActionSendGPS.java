package root.ivatio.ivor.action;

import androidx.collection.CircularArray;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ActionSendGPS extends Action {
    public ActionSendGPS(String cmd, Consumer<String> next) {
        super(cmd);
        subject = PublishSubject.create();
        subject.subscribe(next);
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
