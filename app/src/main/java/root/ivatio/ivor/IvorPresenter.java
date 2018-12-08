package root.ivatio.ivor;

import android.view.View;

import java.util.LinkedList;
import java.util.List;

import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.util.ListsHolder;
import root.ivatio.util.StringProcessor;
import root.ivatio.Message;
import root.ivatio.R;
import root.ivatio.activity.MsgActivity;

public class IvorPresenter {
    private Ivor model;
    private IvorViewAPI viewAPI;
    public IvorPresenter(Ivor ivor, IvorViewAPI api) {
        model = ivor;
        viewAPI = api;
    }

    public void setMenuModeStd() {
        viewAPI.setRole(MsgActivity.ROLE.STD);
        viewAPI.appendMessage(model.send(R.string.ivorModeSTD));
        viewAPI.switchButtonDelete(View.GONE);
    }

    public void setMenuModeIvorAskingKW() {
        viewAPI.setRole(MsgActivity.ROLE.USER_SEND_ANSWER_FOR_KW);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewAnswerForKW));
        viewAPI.appendMessage(model.sendRandomKeyWord());
        viewAPI.switchButtonDelete(View.VISIBLE);
    }
    public void setMenuModeAddKW() {
        viewAPI.setRole(MsgActivity.ROLE.USER_SEND_NEW_KW);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewKW));
        viewAPI.switchButtonDelete(View.GONE);
    }
    public void setMenuModeAddQ() {
        viewAPI.setRole(MsgActivity.ROLE.USER_SEND_NEW_Q);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewQuestion));
        viewAPI.switchButtonDelete(View.GONE);
    }
    public void setMenuModeIvorAskingQ() {
        viewAPI.setRole(MsgActivity.ROLE.USER_SEND_ANSWER_FOR_Q);
        viewAPI.appendMessage(model.send(R.string.ivorModeUserSendNewAnswerForQ));
        viewAPI.appendMessage(model.sendRandomQuestion());
        viewAPI.switchButtonDelete(View.VISIBLE);
    }

    public void onStop() {
        if (model.getCountEval() > Ivor.criticalCountEval)
            model.selection();
    }

    public void clickEval(int eval) {

        if (model.processingKeyWord())
            model.reEvalutionKeyWord(eval);
        if (model.processingQuestion())
            model.reEvalutionQuestion(eval);
        viewAPI.removeRating();
        viewAPI.switchButtonDelete(View.GONE);
    }
    public void clickDelete(MsgActivity.ROLE curRole) {
        viewAPI.appendMessage(model.send(R.string.ivorSuccessfulDelete));
        switch (curRole) {
            case USER_SEND_ANSWER_FOR_KW:
                model.deleteLastKeyWord();
                viewAPI.appendMessage(model.send(model.getRandomKeyWord().content));
                break;
            case USER_SEND_ANSWER_FOR_Q:
                model.deleteLastQuestion();
                viewAPI.appendMessage(model.send(model.getRandomQuestion().content));
                break;
            case STD:
                viewAPI.showMessage(R.string.deprecated);
                model.deleteLastCommunication();
                break;
            default:
        }
    }

    public void clickSend(MsgActivity.ROLE curRole, String request) {
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

    private void sendWithModeSTD(String request) {
        Message newMessage = model.answer(request);
        if (newMessage != null)
            viewAPI.appendMessage(newMessage);
        if (model.processingKeyWord() || model.processingQuestion()) {
            viewAPI.appendRating();
            viewAPI.switchButtonDelete(View.VISIBLE);
        }
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

    public void selectionCommunications() {
        if (model.getCountEval() > Ivor.criticalCountEval) {
            model.selection();
            model.resetCountEval();
            viewAPI.appendMessage(model.send(R.string.ivorSuccessfulSelection));
            viewAPI.showMessage(R.string.successfulSelection);
        }
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
}
