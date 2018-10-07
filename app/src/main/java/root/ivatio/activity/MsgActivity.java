package root.ivatio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bd.answer.Answer;
import bd.communication_key.CommunicationKey;
import bd.key_word.KeyWord;
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
    private ROLE curRole = ROLE.USER_ASKING;

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
            case USER_ASKING:
                messages.append(ivor.answer(inputText.getText().toString()));
                messages.notifyDataSetChanged();
                if (ivor.haveKeyWords())
                    appendRating();
                break;
            case IVOR_ASKING:
                long answerID = App.getDB().getAnswerDao().insert(new Answer(inputText.getText().toString()));

                CommunicationKey comKey = new CommunicationKey(ivor.getLastKeyWord().id ,answerID);
                App.getDB().getCommunicationKeyDao().insert(comKey);
                messages.append(ivor.successfulAnswer());
                messages.append(ivor.getRandomKeyWord());
                break;
            case ADD_KW:
                KeyWord keyWord = new KeyWord(inputText.getText().toString());
                List<KeyWord> keyWords = App.getDB().getKeyWordDao().getAll();
                if (!keyWords.contains(keyWord)) {
                    App.getDB().getKeyWordDao().insert(keyWord);
                    messages.append(ivor.send(R.string.ivorSuccessfulAppendKW));
                } else
                    messages.append(ivor.send(R.string.ivorKWExisting));
                break;
            default:
        }
        inputText.setText("");

    }

    @BindView(R.id.buttonYes)
    ImageButton buttonYes;
    @OnClick(R.id.buttonYes)
    public void clickYes() {
        reEvalutionKeyWord(1);
    }

    @BindView(R.id.buttonNo)
    ImageButton buttonNo;
    @OnClick(R.id.buttonNo)
    public void clickNo() {
        reEvalutionKeyWord(-1);
    }

    private void reEvalutionKeyWord(int eval) {
        ivor.reEvaluationKeyWord(eval, App.getDB().getAnswerDao().getAnswer(messages.getLast().content));
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
                curRole = ROLE.USER_ASKING;
                messages.append(ivor.userNowAsking());
                break;
            case R.id.menuModeIvorAskingKW:
                curRole = ROLE.IVOR_ASKING;
                messages.append(ivor.ivorNowAsking());
                messages.append(ivor.getRandomKeyWord());
                removeRating();
                break;
            case R.id.menuModeAddKW:
                curRole = ROLE.ADD_KW;
                messages.append(ivor.send(R.string.ivorModeAddKW));
                removeRating();
                break;
        }

        return true;
    }

    private Date getCurDate() {
        return Calendar.getInstance().getTime();
    }

    public enum ROLE {
        USER_ASKING,
        IVOR_ASKING,
        ADD_KW
    }
}
