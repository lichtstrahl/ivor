package root.ivatio.network;


import javax.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import root.ivatio.App;
import root.ivatio.util.ListsHolder;

public class ListHolderObserver implements Observer<ListsHolder> {
    private Disposable disposable;
    @Nullable
    private Consumer<Throwable> error;
    @Nullable
    private Action complete;

    public ListHolderObserver(@Nullable Action complete, @Nullable Consumer<Throwable> error) {
        this.error = error;
        this.complete = complete;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ListsHolder listsHolder) {
        // Clear db
        App.getDB().getAnswerDao().deleteAll();
        App.getDB().getCommandDao().deleteAll();
        App.getDB().getCommunicationDao().deleteAll();
        App.getDB().getCommunicationKeyDao().deleteAll();
        App.getDB().getKeyWordDao().deleteAll();
        App.getDB().getQuestionDao().deleteAll();

        // Insert new data
        App.getDB().getAnswerDao().insert(listsHolder.getAnswers());
        App.getDB().getCommandDao().insert(listsHolder.getCommands());
        App.getDB().getCommunicationDao().insert(listsHolder.getCommunications());
        App.getDB().getCommunicationKeyDao().insert(listsHolder.getCommunicationKeys());
        App.getDB().getKeyWordDao().insert(listsHolder.getKeyWords());
        App.getDB().getQuestionDao().insert(listsHolder.getQuestions());
        try {
            if (complete != null) complete.run();
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
