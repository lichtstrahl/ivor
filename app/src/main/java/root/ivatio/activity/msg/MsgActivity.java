package root.ivatio.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
        final String request = inputText.getText().toString();
        if (!request.isEmpty()) {
            ivorPresenter.sendWithStdMode(request);
        }
    }
    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
//        ivorPresenter.clickDelete(curRole);
        scrollListMessagesToBottom();
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

    @Override
    public void setRole(UserRoles role) {
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
}
