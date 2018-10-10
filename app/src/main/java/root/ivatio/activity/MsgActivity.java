package root.ivatio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bd.answer.Answer;
import bd.communication.Communication;
import bd.communication_key.CommunicationKey;
import bd.key_word.KeyWord;
import bd.qustion.Question;
import bd.users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ivor.Ivor;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.MessageAdapter;
import root.ivatio.R;



public class MsgActivity extends AppCompatActivity {
    private User user;
    private Ivor ivor;
    private ROLE curRole = ROLE.STD;

    @BindView(R.id.list)
    ListView listView;
    MessageAdapter messages;

    @BindView(R.id.input)
    EditText inputText;

    @BindView(R.id.personName)
    TextView nameView;
    @BindView(R.id.personAge)
    TextView ageView;


    @OnClick(R.id.buttonSend)
    public void sendClick() {
        messages.append(new Message(user, inputText.getText().toString(), getCurDate()));
        switch (curRole) {
            case STD: {
                messages.append(ivor.answer(inputText.getText().toString()));
                messages.notifyDataSetChanged();
                if (ivor.processingKeyWord() || ivor.processingQuestion()) {
                    appendRating();
                    buttonDelete.setVisibility(View.VISIBLE);
                }
                break;
            }
            case USER_SEND_ANSWER_FOR_KW: {
                Answer answer = new Answer(inputText.getText().toString());
                long answerID = App.getDB().getAnswerDao().insert(answer);
                CommunicationKey comKey = new CommunicationKey(ivor.getLastKeyWord().id, answerID);
                App.getDB().getCommunicationKeyDao().insert(comKey);
                messages.append(ivor.send(R.string.ivorSuccessfulAnswer));
                messages.append(ivor.sendRandomKeyWord());
                break;
            }
            case USER_SEND_NEW_KW: {
                KeyWord keyWord = new KeyWord(
                        Ivor.StringProcessor.toStdFormat(inputText.getText().toString())
                );
                List<KeyWord> keyWords = App.getDB().getKeyWordDao().getAll();
                if (!keyWords.contains(keyWord)) {
                    App.getDB().getKeyWordDao().insert(keyWord);
                    messages.append(ivor.send(R.string.ivorSuccessfulAppendKW));
                } else
                    messages.append(ivor.send(R.string.ivorKWExisting));
                break;
            }
            case USER_SEND_NEW_Q: {
                Question question = new Question(
                        Ivor.StringProcessor.toStdFormat(inputText.getText().toString())
                );
                List<Question> questions = App.getDB().getQuestionDao().getAll();
                if (!questions.contains(question)) {
                    App.getDB().getQuestionDao().insert(question);
                    messages.append(ivor.send(R.string.ivorSuccessfulAppendQ));
                } else
                    messages.append(ivor.send(R.string.ivorQExisting));
                break;
            }
            case USER_SEND_ANSWER_FOR_Q: {
                Answer answer = new Answer(inputText.getText().toString());
                long answerID = App.getDB().getAnswerDao().insert(answer);
                Communication communication = new Communication(ivor.getLastQuestion().id, answerID);
                App.getDB().getCommunicationDao().insert(communication);
                messages.append(ivor.send(R.string.ivorSuccessfulAnswer));
                messages.append(ivor.sendRandomQuestion());
                break;
            }
            default:
        }
        inputText.setText("");

    }

    @BindView(R.id.buttonDelete)
    ImageButton buttonDelete;
    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
        messages.append(ivor.send(R.string.ivorSuccessfulDelete));
        switch (curRole) {
            case USER_SEND_ANSWER_FOR_KW:
                App.getDB().getKeyWordDao().delete(ivor.getLastKeyWord());
                messages.append(ivor.send(ivor.getRandomKeyWord().content));
                break;
            case USER_SEND_ANSWER_FOR_Q:
                App.getDB().getQuestionDao().delete(ivor.getLastQuestion());
                messages.append(ivor.send(ivor.getRandomQuestion().content));
                break;
            case STD:
                Toast.makeText(this, R.string.deprecated, Toast.LENGTH_SHORT).show();
                bd.Communication lastCom = ivor.getLastCommunication();
                if(lastCom.getType() == bd.Communication.COMMUNICATION)
                    App.getDB().getCommunicationDao().delete(lastCom.getID());
                else
                    App.getDB().getCommunicationKeyDao().delete(lastCom.getID());
                break;
            default:
        }
    }

    @BindView(R.id.buttonYes)
    ImageButton buttonYes;
    @OnClick(R.id.buttonYes)
    public void clickYes() {
        if (ivor.processingKeyWord())
            reEvalutionKeyWord(1);
        if (ivor.processingQuestion())
            reEvalutionQuestion(1);

    }

    @BindView(R.id.buttonNo)
    ImageButton buttonNo;
    @OnClick(R.id.buttonNo)
    public void clickNo() {
        if (ivor.processingKeyWord())
            reEvalutionKeyWord(-1);
        if (ivor.processingQuestion())
            reEvalutionQuestion(-1);
    }

    private void reEvalutionKeyWord(int eval) {
        ivor.reEvaluationKeyWord(eval);
        removeRating();
    }
    private void reEvalutionQuestion(int eval) {
        ivor.reEvalutionQuestion(eval);
        removeRating();
    }

    private void removeRating() {
        buttonNo.setVisibility(View.INVISIBLE);
        buttonYes.setVisibility(View.INVISIBLE);
    }
    private void appendRating() {
        buttonNo.setVisibility(View.VISIBLE);
        buttonYes.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);

        user = App.getDB().getUserDao().getUser(getIntent().getLongExtra(App.USER_INDEX, -1));
        nameView.setText(user.realName);
        ageView.setText(String.valueOf(user.age));

        ivor = new Ivor(getResources());

        messages = new MessageAdapter(this, new ArrayList<Message>());
        listView.setAdapter(messages);

        removeRating();
        setTitle(R.string.dialog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.msg_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        messages.clear();
        switch (item.getItemId()) {
            case R.id.menuModeStd:
                curRole = ROLE.STD;
                messages.append(ivor.send(R.string.ivorModeSTD));
                buttonDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.menuModeIvorAskingKW:
                curRole = ROLE.USER_SEND_ANSWER_FOR_KW;
                messages.append(ivor.send(R.string.ivorModeUserSendNewAnswerForKW));
                messages.append(ivor.sendRandomKeyWord());
                buttonDelete.setVisibility(View.VISIBLE);
                break;
            case R.id.menuModeAddKW:
                curRole = ROLE.USER_SEND_NEW_KW;
                messages.append(ivor.send(R.string.ivorModeUserSendNewKW));
                buttonDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.menuModeAddQuestion:
                curRole = ROLE.USER_SEND_NEW_Q;
                messages.append(ivor.send(R.string.ivorModeUserSendNewQuestion));
                buttonDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.menuModeIvorAskingQ:
                curRole = ROLE.USER_SEND_ANSWER_FOR_Q;
                messages.append(ivor.send(R.string.ivorModeUserSendNewAnswerForQ));
                messages.append(ivor.sendRandomQuestion());
                buttonDelete.setVisibility(View.VISIBLE);
                break;
            default:
        }
        removeRating();
        return true;
    }

    private Date getCurDate() {
        return Calendar.getInstance().getTime();
    }

    public enum ROLE {
        STD,
        USER_SEND_ANSWER_FOR_KW,
        USER_SEND_NEW_KW,
        USER_SEND_ANSWER_FOR_Q,
        USER_SEND_NEW_Q,
    }
}
