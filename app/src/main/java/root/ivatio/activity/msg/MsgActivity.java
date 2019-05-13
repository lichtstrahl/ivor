package root.ivatio.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.R;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.ivor.action.ActionCall;
import root.ivatio.ivor.presenter.IvorReactivePresenter;

public class MsgActivity extends AppCompatActivity implements IvorViewAPI {
    private static final String INTENT_USER = "args:user";
    private User user;
    private IvorReactivePresenter ivorPresenter;
    @BindView(R.id.list)
    protected RecyclerView listView;
    private MessageAdapter messages;
    @BindView(R.id.input)
    protected EditText inputText;
    @BindView(R.id.progressLoad)
    protected ProgressBar progressLoad;
    @BindView(R.id.buttonNo)
    protected ImageButton buttonNo;
    @BindView(R.id.buttonYes)
    protected ImageButton buttonYes;
    @BindView(R.id.buttonDelete)
    protected ImageButton buttonDelete;
    @BindView(R.id.buttonSend)
    protected ImageButton buttonSend;
    private UserRoles currentRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        final Ivor ivor = new Ivor();
        ivorPresenter = new IvorReactivePresenter(ivor, this);
        messages = new MessageAdapter(this, new ArrayList<>());
        listView.setAdapter(messages);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) lManager).setStackFromEnd(true);
        listView.setLayoutManager(lManager);
        hideRating();

        user = (User)getIntent().getSerializableExtra(INTENT_USER);
        currentRole = UserRoles.STD;
        setTitle(user.login + ", " + user.realName);
    }

    public static void start(Context context, User user) {
        Intent msgIntent = new Intent(context, MsgActivity.class);
        msgIntent.putExtra(INTENT_USER, user);
        context.startActivity(msgIntent);
    }

    @OnClick(R.id.buttonSend)
    public void sendClick() {
        final String request = inputText.getText().toString();
        if (!request.isEmpty()) {
            ivorPresenter.sendClick(currentRole, request);
        }
    }
    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
        scrollListMessagesToBottom();
    }

    @OnClick(R.id.buttonYes)
    public void clickYes() {
        ivorPresenter.clickEval(1);
    }

    @OnClick(R.id.buttonNo)
    public void clickNo() {
        ivorPresenter.clickEval(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.msg_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuModeStd:
                switchRole(UserRoles.STD);
                appendMessage(Message.getIvorMessage(getString(R.string.setStdMode)));
                break;

            case R.id.menuModeAddAnswer:
                switchRole(UserRoles.ADD_ANSWER);
                appendMessage(Message.getIvorMessage(getString(R.string.setAddAnswerMode)));
                break;
            default:
        }
        return true;
    }

    /** Реализация интерфейса IvorViewAPI **/
    /***************************************/
    @Override
    public void hideRating() {
        buttonNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.GONE);
    }

    @Override
    public void showRating() {
        buttonNo.setVisibility(View.VISIBLE);
        buttonYes.setVisibility(View.VISIBLE);
    }

    @Override
    public void appendMessage(Message msg) {
        messages.append(msg);
    }

    @Override
    public void clearInputFild() {
        inputText.setText("");
    }

    @Override
    public void appendUserMessage(String input) {
        messages.append(new Message(user, input));
    }

    @Override
    public void scrollListMessagesToBottom() {
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
    }

    public void switchRole(UserRoles role) {
        currentRole = role;
        messages.clear();
        clearInputFild();
        hideRating();
        scrollListMessagesToBottom();
    }
}
