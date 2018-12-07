package root.ivatio.network;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import root.ivatio.App;
import root.ivatio.bd.users.User;

public class UserPostObserver implements SingleObserver<User> {
    private Disposable disposable;
    private Consumer<Throwable> error;
    private Consumer<User> success;

    public UserPostObserver(Consumer<User> success, Consumer<Throwable> error) {
        this.success = success;
        this.error = error;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(User user) {
        try {
            success.accept(user);
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
        disposable.dispose();
    }
}
