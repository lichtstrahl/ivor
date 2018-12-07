package root.ivatio.ivor.action;

import android.support.v4.util.CircularArray;

import io.reactivex.subjects.PublishSubject;

abstract public class Action implements ActionAPI {
    protected String cmd;
    protected String endMessage;
    protected PublishSubject<String> subject;
    protected CircularArray<String> buffer;
    public Action(String c) {
        cmd = c;
        endMessage = "";
        subject = PublishSubject.create();
    }
    public String getCmd() {
        return cmd;
    }

    @Override
    public String next() {
        String str = buffer.popLast();
        subject.onNext(str);
        buffer.addFirst(str);
        return "";
    }
}
