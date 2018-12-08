package root.ivatio.network;


import javax.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import root.ivatio.App;

public class NetworkObserver<T> implements Observer<T> {
    private Disposable disposable;
    @Nullable
    private Consumer<Throwable> error;
    @Nullable
    private Consumer<T> complete;

    public NetworkObserver(@Nullable Consumer<T> complete, @Nullable Consumer<Throwable> error) {
        this.error = error;
        this.complete = complete;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T result) {
        try {
            if (complete != null) complete.accept(result);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (error != null) error.accept(t);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
    }

    public void unsubscribe() {
        disposable.dispose();
    }
}
