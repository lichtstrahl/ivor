package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import io.reactivex.functions.Consumer;
import root.ivatio.App;

abstract public class Action implements ActionAPI {
    protected String cmd;
    protected String endMessage;
    protected CircularArray<String> buffer;
    protected Consumer<String> complete;

    public Action(String c, Consumer<String> next) {
        cmd = c;
        endMessage = "";
        this.complete = next;
    }


    public String getCmd() {
        return cmd;
    }

    @Override
    public String next() {
        String str = buffer.popLast();
        try {
            complete.accept(str);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
        buffer.addFirst(str);
        return str;
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
        }),
new ActionSendSMS(getString(R.string.cmdSendSMS),
        x -> {
            if (x.isEmpty()) {
                List<String> param = ivorPresenter.completeAction();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(param.get(0), null, param.get(1), null, null);
                Toast.makeText(this, R.string.successfulSendSMS, Toast.LENGTH_SHORT).show();
            } else
                appendMessage(new Message(null, x));
        }),
new ActionSendEmail(getString(R.string.cmdSendEmail), x-> {
    if (x.isEmpty()) {
        List<String> param = ivorPresenter.completeAction();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {param.get(0)});
        intent.putExtra(Intent.EXTRA_SUBJECT, param.get(1));
        intent.putExtra(Intent.EXTRA_TEXT, param.get(2));
        this.startActivity(Intent.createChooser(intent, "Отправка ..."));
    } else
        appendMessage(new Message(null, x));
}),
new ActionSendGPS(getString(R.string.cmdSendGPS), x-> {
    if (x.isEmpty()) {
        ivorPresenter.completeAction();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+user.city));
        startActivity(intent);
    } else
        appendMessage(new Message(null, x));
})
 */