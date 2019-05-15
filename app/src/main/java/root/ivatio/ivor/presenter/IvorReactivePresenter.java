package root.ivatio.ivor.presenter;

import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.activity.msg.Message;
import root.ivatio.activity.msg.UserRoles;
import root.ivatio.app.App;
import root.ivatio.bd.qustion.Question;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.network.dto.AnswerDTO;
import root.ivatio.network.dto.AnswerForQuestionDTO;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.RequestDTO;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

public class IvorReactivePresenter extends Presenter implements Subscribed {
    private SingleNetworkObserver<ServerAnswerDTO> requestObserver;

    public IvorReactivePresenter(Ivor ivor, IvorViewAPI api) {
        super(ivor, api);

        requestObserver = new SingleNetworkObserver<>(this::successfulRequest, this::errorNetwork);
    }

    public void sendClick(UserRoles role, String request) {
        switch (role) {
            case STD:
                sendWithStdMode(request);
                break;
            case ADD_ANSWER:
                sendWithAddAnswerMode(request);
                break;
        }
    }

    public void clickEval(int eval) {
        Question q = model.getLastQuestion();
        if (q != null) {
//            App.getServerAPI().evaluation(new EvaluationDTO(q.id, eval))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(SingleNetworkObserver.observerEmpty(this::successfulEvaluation, this::errorNetwork));
        }
    }

    @Override
    public void unsubscribe() {
        requestObserver.unsubscribe();
    }

    // PRIVATE
    private void sendWithStdMode(String request) {
        viewAPI.appendUserMessage(request);
        viewAPI.clearInputFild();
        App.getServerAPI().request(RequestDTO.fromString(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestObserver);
    }

    private void sendWithAddAnswerMode(String request) {
        viewAPI.appendUserMessage(request);
        viewAPI.clearInputFild();
//        App.getServerAPI().insertAnswer(new ContentDTO(request))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(SingleNetworkObserver.observerEmpty(this::successfulInserAnswer, this::errorNetwork));
    }

    private void successfulRequest(ServerAnswerDTO response) {
        AnswerDTO answer = AnswerDTO.parseJSON((LinkedTreeMap<String, Object>) response.getData());
        App.logI("Response: " + answer.getAnswer());
        viewAPI.appendMessage(Message.getIvorMessage(answer.getAnswer()));
        viewAPI.scrollListMessagesToBottom();
    }

    private void successfulEvaluation(ServerAnswerDTO<EmptyDTO> response) {
        viewAPI.hideRating();
    }

    private void successfulInserAnswer(ServerAnswerDTO<EmptyDTO> response) {
        viewAPI.appendMessage(Message.getIvorMessage("Ответ успешно добавлен. Жду новых знаний!"));
        viewAPI.scrollListMessagesToBottom();
    }

    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }
}
