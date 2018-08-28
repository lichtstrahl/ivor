package root.ivatio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    }

    private Date getCurDate() {
        return Calendar.getInstance().getTime();
    }
}
