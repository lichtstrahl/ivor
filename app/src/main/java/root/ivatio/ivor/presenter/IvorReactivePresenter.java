package root.ivatio.ivor.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.activity.msg.Message;
import root.ivatio.app.App;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.network.dto.AnswerDTO;
import root.ivatio.network.dto.RequestDTO;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

public class IvorReactivePresenter extends Presenter {
    private SingleNetworkObserver<ServerAnswerDTO<AnswerDTO>> requestObserver;

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

    private void successfulNetwork(ServerAnswerDTO<AnswerDTO> respone) {
        final String answer = respone.getData().getAnswer();
        App.logI("Response: " + respone.getData().getAnswer());
        viewAPI.appendMessage(Message.getIvorMessage(answer));
        viewAPI.scrollListMessagesToBottom();
    }

    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }
}
