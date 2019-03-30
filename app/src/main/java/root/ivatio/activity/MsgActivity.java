package root.ivatio.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.Message;
import root.ivatio.MessageAdapter;
import root.ivatio.R;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.ivor.action.ActionCall;
import root.ivatio.ivor.action.ActionSendEmail;
import root.ivatio.ivor.action.ActionSendGPS;
import root.ivatio.ivor.action.ActionSendSMS;
import root.ivatio.ivor.presenter.IvorReactivePresenter;

public class MsgActivity extends AppCompatActivity implements IvorViewAPI {
    private static final String INTENT_USER = "args:user";
    private User user;
    private IvorReactivePresenter ivorPresenter;
    @BindView(R.id.list)
    RecyclerView listView;
    MessageAdapter messages;
    @BindView(R.id.input)
    EditText inputText;
    @BindView(R.id.progressLoad)
    ProgressBar progressLoad;
    @BindView(R.id.buttonNo)
    ImageButton buttonNo;
    @BindView(R.id.buttonYes)
    ImageButton buttonYes;
    @BindView(R.id.buttonDelete)
    ImageButton buttonDelete;
    @BindView(R.id.buttonSend)
    ImageButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        final Ivor ivor = new Ivor(getResources(), new ActionCall("", s->{}));
        ivorPresenter = new IvorReactivePresenter(ivor, this);
        messages = new MessageAdapter(this, new ArrayList<>());
        listView.setAdapter(messages);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) lManager).setStackFromEnd(true);
        listView.setLayoutManager(lManager);
        removeRating();

        user = (User)getIntent().getSerializableExtra(INTENT_USER);
        setTitle(user.login + ", " + user.realName);
    }

    public static void start(Context context, User user) {
        Intent msgIntent = new Intent(context, MsgActivity.class);
        msgIntent.putExtra(INTENT_USER, user);
        context.startActivity(msgIntent);
    }

    @OnClick(R.id.buttonSend)
    public void sendClick() {
        if (inputText.getText().toString().isEmpty())
            return;
        final String request = inputText.getText().toString();
        messages.append(new Message(user, request, getCurDate()));
//        ivorPresenter.clickSend(curRole, inputText.getText().toString());
        ivorPresenter.sendWithStdMode(request);
        inputText.setText("");
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
    }
    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
//        ivorPresenter.clickDelete(curRole);
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
    }

    @OnClick(R.id.buttonYes)
    public void clickYes() {
//        ivorPresenter.clickEval(1);
    }

    @OnClick(R.id.buttonNo)
    public void clickNo() {
//        ivorPresenter.clickEval(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (user.isAdmin())
            getMenuInflater().inflate(R.menu.msg_options_menu, menu);
        return true;
    }

    public enum ROLE {
        STD,
        USER_SEND_ANSWER_FOR_KW,
        USER_SEND_NEW_KW,
        USER_SEND_ANSWER_FOR_Q,
        USER_SEND_NEW_Q,
    }

    /** Реализация интерфейса IvorViewAPI **/
    /***************************************/
    @Override
    public void removeRating() {
        buttonNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.GONE);
    }

    @Override
    public void appendRating() {
        buttonNo.setVisibility(View.VISIBLE);
        buttonYes.setVisibility(View.VISIBLE);
    }

    @Override
    public void appendMessage(Message msg) {
        messages.append(msg);
    }

    private Date getCurDate() {
        return Calendar.getInstance().getTime();
    }

    @Override
    public void setRole(ROLE role) {
    }

    @Override
    public void switchButtonDelete(int visibility) {
        if (visibility == View.VISIBLE && !user.isAdmin())
            return;
        buttonDelete.setVisibility(visibility);
    }

    @Override
    public void showMessage(int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void needEval() {
        Toast.makeText(this, R.string.needEval, Toast.LENGTH_SHORT).show();
    }
}
