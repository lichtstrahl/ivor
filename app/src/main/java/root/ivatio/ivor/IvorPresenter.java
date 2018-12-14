package root.ivatio.ivor;

import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.App;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.network.NetworkObserver;
import root.ivatio.util.ListsHolder;
import root.ivatio.util.ROLE;
import root.ivatio.util.StringProcessor;
import root.ivatio.Message;
import root.ivatio.R;

public class IvorPresenter {
    private Ivor model;
    private IvorViewAPI viewAPI;
    private NetworkObserver<String> answerObserver;
    private NetworkObserver<String> evalObserver;

    public IvorPresenter(Ivor ivor, IvorViewAPI api) {
        model = ivor;
        viewAPI = api;
        answerObserver = new NetworkObserver<>(this::successfulAnswer, this::errorNetwork);
        evalObserver = new NetworkObserver<>(this::successfulEval, this::errorEval);
    }

    public void setMenuModeStd() {
        viewAPI.setRole(ROLE.STD);
        viewAPI.appendMessage(model.send(R.string.ivorModeSTD));
        viewAPI.switchButtonDelete(View.GONE);
    }

    public void setMenuModeIvorAskingKW() {
        if (App.getStorageAPI().getKeyWords().isEmpty())
            return;
        viewAPI.setRole(ROLE.USER_SEND_ANSWER_FOR_KW);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewAnswerForKW));
        viewAPI.appendMessage(model.sendRandomKeyWord());
        viewAPI.switchButtonDelete(View.VISIBLE);
    }

    public void setMenuModeAddKW() {
        viewAPI.setRole(ROLE.USER_SEND_NEW_KW);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewKW));
        viewAPI.switchButtonDelete(View.GONE);
    }

    public void setMenuModeAddQ() {
        viewAPI.setRole(ROLE.USER_SEND_NEW_Q);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewQuestion));
        viewAPI.switchButtonDelete(View.GONE);
    }

    public void setMenuModeIvorAskingQ() {
        if (App.getStorageAPI().getQuestions().isEmpty())
            return;
        viewAPI.setRole(ROLE.USER_SEND_ANSWER_FOR_Q);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewAnswerForQ));
        viewAPI.appendMessage(model.sendRandomQuestion());
        viewAPI.switchButtonDelete(View.VISIBLE);
    }

    public void onStop() {
        if (model.getCountEval() > Ivor.CRITICAL_COUNT_EVAL) {
            model.selection();
        }
        model.insertCommunication();
        model.unsubscribe();
    }

    public void clickDelete(ROLE curRole) {
        viewAPI.appendMessage(model.send(R.string.ivorSuccessfulDelete));
        switch (curRole) {
            case USER_SEND_ANSWER_FOR_KW:
                model.deleteLastKeyWord();
                viewAPI.appendMessage(model.sendRandomKeyWord());
                break;
            case USER_SEND_ANSWER_FOR_Q:
                model.deleteLastQuestion();
                viewAPI.appendMessage(model.sendRandomQuestion());
                break;
            case STD:
                viewAPI.showMessage(R.string.deprecated);
                model.deleteLastCommunication();
                break;
            default:
        }
        viewAPI.removeRating();
        viewAPI.switchButtonDelete(View.GONE);
    }

    public void clickSend(ROLE curRole, String request) {
        switch (curRole) {
            case STD:
                sendWithModeSTD(request);
                break;
            case USER_SEND_ANSWER_FOR_KW:
                sendWithModeUserSendAnswerForKW(request);
                break;
            case USER_SEND_NEW_KW:
                sendWithModeUserSendNewKW(request);
                break;
            case USER_SEND_NEW_Q:
                sendWithModeUserSendNewQ(request);
                break;
            case USER_SEND_ANSWER_FOR_Q:
                sendWithModeUserSendAnswerForQ(request);
                break;
            default:
        }
    }

    public void clickEval(int eval) {
        viewAPI.switchProgress(View.VISIBLE);
        model.evaluation(eval)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(evalObserver);
    }

    private void sendWithModeSTD(String request) {
        viewAPI.switchProgress(View.VISIBLE);
        model.rxAnswer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(answerObserver);
    }

    private void sendWithModeUserSendAnswerForKW(String request) {
        model.appendNewAnswerForLastKW(new Answer(request));
        viewAPI.appendMessage(model.send(R.string.ivorSuccessfulAnswer));
        viewAPI.appendMessage(model.sendRandomKeyWord());
    }

    private void sendWithModeUserSendNewKW(String request) {
        KeyWord keyWord = new KeyWord(StringProcessor.toStdFormat(request));
        if (model.appendNewKeyWord(keyWord))
            viewAPI.appendMessage(model.send(R.string.ivorSuccessfulAppendKW));
        else
            viewAPI.appendMessage(model.send(R.string.ivorKWExisting));
    }

    private void sendWithModeUserSendNewQ(String request) {
        Question question = new Question(StringProcessor.toStdFormat(request));
        if (model.appendNewQuestion(question))
            viewAPI.appendMessage(model.send(R.string.ivorSuccessfulAppendQ));
        else
            viewAPI.appendMessage(model.send(R.string.ivorQExisting));
    }

    private void sendWithModeUserSendAnswerForQ(String request) {
        model.appendNewAnswerForLastQ(new Answer(request));
        viewAPI.appendMessage(model.send(R.string.ivorSuccessfulAnswer));
        viewAPI.appendMessage(model.sendRandomQuestion());
    }

    public void selection() {
        if (model.getCountEval() > Ivor.CRITICAL_COUNT_EVAL) {
            model.selection();
            model.resetCountEval();
            viewAPI.appendMessage(model.send(R.string.ivorSuccessfulSelection));
            viewAPI.showMessage(R.string.successfulSelection);
        }
    }

    public void selectionForce() {
        model.selection();
        model.resetCountEval();
        viewAPI.appendMessage(model.send(R.string.ivorSuccessfulSelection));
        viewAPI.showMessage(R.string.successfulSelection);
    }

    public List<String> completeAction() {
        return model.resetAction();
    }

    public ListsHolder getNewElements() {
        return ListsHolder.getBuilder()
                .buildAnswers(model.getNewAnswers())
                .buildCommands(new LinkedList<>())
                .buildCommunications(model.getNewCommunications())
                .buildCommunicationKeys(model.getNewCommunicationKeys())
                .buildKeyWords(model.getNewKeyWords())
                .buildQuestions(model.getNewQuestions())
                .build();
    }

    private void successfulAnswer(String answer) {
        viewAPI.switchProgress(View.GONE);
        if (!answer.isEmpty()) viewAPI.appendMessage(new Message(null, answer));
        if (model.processingKeyWord() || model.processingQuestion()) {
            viewAPI.appendRating();
            viewAPI.switchButtonDelete(View.VISIBLE);
        }
    }

    private void successfulEval(String response) {
        if (response.isEmpty())
            viewAPI.showMessage(R.string.notFoundForEval);
        viewAPI.switchProgress(View.GONE);
        viewAPI.removeRating();
        viewAPI.switchButtonDelete(View.GONE);
    }

    private void errorEval(Throwable t) {
        viewAPI.switchProgress(View.GONE);
        viewAPI.removeRating();
        viewAPI.switchButtonDelete(View.GONE);
        App.logI(t.getMessage());
    }

    private void errorNetwork(Throwable t) {
        viewAPI.switchProgress(View.GONE);
        App.logI(t.getMessage());
    }
}
