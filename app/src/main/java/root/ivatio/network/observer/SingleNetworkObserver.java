package root.ivatio.network.observer;

import androidx.annotation.Nullable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import root.ivatio.app.App;
import root.ivatio.network.dto.AnswerForQuestionDTO;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.ServerAnswerDTO;

public class SingleNetworkObserver<T> implements SingleObserver<T> {
    @Nullable
    private Disposable disposable;
    private Consumer<Throwable> error;
    private Consumer<T> success;

    public SingleNetworkObserver(Consumer<T> success, Consumer<Throwable> error) {
        this.success = success;
        this.error = error;
    }

    public static SingleNetworkObserver<ServerAnswerDTO<EmptyDTO>> observerEmpty(Consumer<ServerAnswerDTO<EmptyDTO>> success, Consumer<Throwable> error) {
        return  new SingleNetworkObserver<>(success, error);
    }

    public static SingleNetworkObserver<ServerAnswerDTO<AnswerForQuestionDTO>> observerRequest(Consumer<ServerAnswerDTO<AnswerForQuestionDTO>> success, Consumer<Throwable> error) {
        return new SingleNetworkObserver<>(success, error);
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
