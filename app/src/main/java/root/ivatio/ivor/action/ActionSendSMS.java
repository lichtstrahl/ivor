package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ActionSendSMS extends Action {
    private String phoneNumber;
    private String smsContent;
    public ActionSendSMS(String cmd, Consumer<String> next) {
        super(cmd);
        subject = PublishSubject.create();
        subject.subscribe(next);
        phoneNumber = null;
        smsContent = null;
        buffer = new CircularArray<>();
        buffer.addFirst("Введите номер адресата");
        buffer.addFirst("А теперь текст сообщения");
        buffer.addFirst(endMessage);
    }

    @Override
    public void put(String param) {
        if (phoneNumber == null)
            phoneNumber = param;
        else if (smsContent == null)
            smsContent = param;
    }

    @Override
    public List<String> getParam() {
        List<String> list = Arrays.asList(phoneNumber, smsContent);
        phoneNumber = smsContent = null;
        return list;
    }
}
