package root.ivatio.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import root.ivatio.App;
import root.ivatio.util.ListsHolder;

public class ListHolderObserver implements Observer<ListsHolder> {
    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ListsHolder listsHolder) {
        App.logI("Holder on next");
    }

    @Override
    public void onError(Throwable e) {
        App.logW(e.getMessage());
    }

    @Override
    public void onComplete() {
    }

    public void unsubscribe() {
    }
}
