package root.ivatio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Ivor.Ivor;
import bd.KeyWord.KeyWord;
import bd.Users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.MessageAdapter;
import root.ivatio.R;

public class MsgActivity extends AppCompatActivity {
    private User user;
    private Ivor ivor;

    @BindView(R.id.list)
    ListView listView;
    ArrayList<Message> messages;

    @BindView(R.id.input)
    AutoCompleteTextView inputText;

    @OnClick(R.id.editDB)
    public void editDBClick() {
        Intent intent = new Intent(this, SelectTable.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonSend)
    public void sendClick() {
        messages.add(new Message(user, inputText.getText().toString(), getCurDate()));
        messages.add(ivor.send(inputText.getText().toString()));
        ((MessageAdapter) listView.getAdapter()).notifyDataSetInvalidated();

        if (ivor.getLastKeyWord() != null) {
            buttonNo.setVisibility(View.VISIBLE);
            buttonYes.setVisibility(View.VISIBLE);
        }

        listView.scrollTo(0, listView.getTop());
    }

    @BindView(R.id.buttonYes)
    ImageButton buttonYes;
    @OnClick(R.id.buttonYes)
    public void clickYes() {
        ivor.re_evalutionKeyWord(1, App.getDB().getAnswerDao().getAnswer(messages.get(messages.size()-1).content));
        buttonYes.setVisibility(View.INVISIBLE);
        buttonNo.setVisibility(View.INVISIBLE);
    }

    @BindView(R.id.buttonNo)
    ImageButton buttonNo;
    @OnClick(R.id.buttonNo)
    public void clickNo() {
        ivor.re_evalutionKeyWord(-1, App.getDB().getAnswerDao().getAnswer(messages.get(messages.size()-1).content));
        buttonNo.setVisibility(View.INVISIBLE);
        buttonYes.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);

        user = App.getDB().getUserDao().getUser(getIntent().getLongExtra(App.USER_INDEX, -1));
        ((TextView)findViewById(R.id.personName)).setText(user.realName);
        ((TextView)findViewById(R.id.personAge)).setText(String.valueOf(user.age));

        ivor = new Ivor(getResources());

        messages = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(this, messages));

        buttonNo.setVisibility(View.INVISIBLE);
        buttonYes.setVisibility(View.INVISIBLE);
    }

    private Date getCurDate() {
        return Calendar.getInstance().getTime();
    }
}
