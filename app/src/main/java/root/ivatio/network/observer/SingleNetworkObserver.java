package root.ivatio.network.observer;

import androidx.annotation.Nullable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import root.ivatio.app.App;

public class SingleNetworkObserver<T> implements SingleObserver<T> {
    @Nullable
    private Disposable disposable;
    private Consumer<Throwable> error;
    private Consumer<T> success;

    public SingleNetworkObserver(Consumer<T> success, Consumer<Throwable> error) {
        this.success = success;
        this.error = error;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(T response) {
        try {
            success.accept(response);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
    }

    @Override
    public void onError(Throwable t) {
        try {
            error.accept(t);
        } catch (Exception e) {
            App.logE(e.getMessage());
        }
    }

    public void unsubscribe() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
