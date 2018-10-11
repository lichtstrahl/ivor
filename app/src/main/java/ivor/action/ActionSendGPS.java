package ivor.action;

import android.support.v4.util.CircularArray;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ActionSendGPS extends Action {
    private String lat;
    private String lon;
    public ActionSendGPS(String cmd, Consumer<String> next) {
        super(cmd);
        subject = PublishSubject.create();
        subject.subscribe(next);
        buffer = new CircularArray<>();
        buffer.addFirst("Введи широту");
        buffer.addFirst("Введи долготу");
        buffer.addFirst(endMessage);
        lat = lon = null;
    }


    @Override
    public void put(String param) {
        if (lat == null)
            lat = param;
        else if (lon == null)
            lon = param;
    }

    @Override
    public List<String> getParam() {
        List<String> list = Arrays.asList(lat, lon);
        lat = lon = null;
        return list;
    }
}
