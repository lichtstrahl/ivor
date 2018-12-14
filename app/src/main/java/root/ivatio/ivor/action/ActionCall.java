package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class ActionCall extends Action {
    private String phoneNumber;

    public ActionCall(String c, Consumer<String> next) {
        super(c, next);

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

/*
new ActionCall(
        getString(R.string.cmdCall),
        x -> {
            if (x.isEmpty()) {
                List<String> param = ivorPresenter.completeAction();
                String dial = "tel:" + param.get(0);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else
                appendMessage(new Message(null, x));
        })
 */