package root.ivatio.ivor.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.activity.msg.Message;
import root.ivatio.app.App;
import root.ivatio.bd.qustion.Question;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.network.dto.AnswerForQuestionDTO;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.EvaluationDTO;
import root.ivatio.network.dto.RequestDTO;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

public class IvorReactivePresenter extends Presenter {
    private SingleNetworkObserver<ServerAnswerDTO<AnswerForQuestionDTO>> requestObserver;
    private SingleNetworkObserver<ServerAnswerDTO<EmptyDTO>> evaluationObserver;

    public IvorReactivePresenter(Ivor ivor, IvorViewAPI api) {
        super(ivor, api);
        requestObserver = new SingleNetworkObserver<>(this::successfulRequest, this::errorNetwork);
        evaluationObserver = new SingleNetworkObserver<>(this::successfulEvaluation, this::errorNetwork);
    }

    public void sendWithStdMode(String request) {
        viewAPI.appendUserMessage(request);
        viewAPI.clearInputFild();
        App.getServerAPI().request(new RequestDTO(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestObserver);
    }

    public void clickEval(int eval) {
        Question q = model.getLastQuestion();
        if (q != null) {
            App.getServerAPI().evaluation(new EvaluationDTO(q.id, eval))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(evaluationObserver);
        }
    }

    private void successfulRequest(ServerAnswerDTO<AnswerForQuestionDTO> response) {
        final String answer = response.getData().getAnswer();
        App.logI("Response: " + response.getData().getAnswer());
        viewAPI.appendMessage(Message.getIvorMessage(answer));
        viewAPI.scrollListMessagesToBottom();

        Long qID = response.getData().getQuestionID();
        if (qID != null) {
            model.saveQuestion(qID);
            viewAPI.showRating();
        }
    }

    private void successfulEvaluation(ServerAnswerDTO<EmptyDTO> response) {
        viewAPI.hideRating();
    }


    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }
}
