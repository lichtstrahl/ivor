package root.ivatio.ivor.presenter;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.activity.msg.Message;
import root.ivatio.app.App;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.network.dto.AnswerDTO;
import root.ivatio.network.dto.AnswerForQuestionDTO;
import root.ivatio.network.dto.RequestDTO;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

public class IvorReactivePresenter extends Presenter {
    private SingleNetworkObserver<ServerAnswerDTO<AnswerForQuestionDTO>> requestObserver;

    public IvorReactivePresenter(Ivor ivor, IvorViewAPI api) {
        super(ivor, api);
        requestObserver = new SingleNetworkObserver<>(this::successfulNetwork, this::errorNetwork);
    }

    public void sendWithStdMode(String request) {
        viewAPI.appendUserMessage(request);
        viewAPI.clearInputFild();
        App.getServerAPI().request(new RequestDTO(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestObserver);
    }

    private void successfulNetwork(ServerAnswerDTO<AnswerForQuestionDTO> response) {
        final String answer = response.getData().getAnswer();
        App.logI("Response: " + response.getData().getAnswer());
        model.saveQuestion(response.getData().getQuestionID());
        viewAPI.appendMessage(Message.getIvorMessage(answer));
        viewAPI.scrollListMessagesToBottom();
    }

    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }
}
