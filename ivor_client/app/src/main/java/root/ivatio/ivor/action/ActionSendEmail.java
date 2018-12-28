package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

// TODO Сделать отписки, при complete
public class ActionSendEmail extends Action {
    private String email;
    private String theme;
    private String body;

    public ActionSendEmail(String cmd, Consumer<String> next) {
        super(cmd, next);
        buffer = new CircularArray<>();
        buffer.addFirst("Введи почту, кому хочешь отправить письмо");
        buffer.addFirst("О чем оно будет?");
        buffer.addFirst("Что же ты хотел написать?");
        buffer.addFirst(endMessage);

        email = theme = body = null;
    }

    @Override
    public void put(String param) {
        if (email == null)
            email = param;
        else if (theme == null)
            theme = param;
        else if (body == null)
            body = param;
    }

    @Override
    public List<String> getParam() {
        List<String> list = Arrays.asList(email, theme, body);
        email = theme = body = null;
        return list;
    }
}
