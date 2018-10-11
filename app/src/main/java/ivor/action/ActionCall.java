package ivor.action;

import android.support.v4.util.CircularArray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ActionCall extends Action {
    private String phoneNumber;

    public ActionCall(String c, Consumer<String> next) {
        super(c);

        subject.subscribe(next);
        phoneNumber = null;
        buffer = new CircularArray<>();
        buffer.addFirst("Введите номер");
        buffer.addFirst(endMessage);
    }

    @Override
    public void put(String param) {
        if (phoneNumber == null)
            phoneNumber = param;
    }

    @Override
    public List<String> getParam() {
        List<String> list = Arrays.asList(phoneNumber);
        phoneNumber = null;
        return new LinkedList<>(list);
    }
}
