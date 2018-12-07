package root.ivatio.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import root.ivatio.bd.users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorPresenter;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.ivor.action.ActionCall;
import root.ivatio.ivor.action.ActionSendEmail;
import root.ivatio.ivor.action.ActionSendGPS;
import root.ivatio.ivor.action.ActionSendSMS;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.MessageAdapter;
import root.ivatio.R;
// TODO Убрать обращение к локальной БД в onCreate
public class MsgActivity extends AppCompatActivity implements IvorViewAPI {
    private static final String INTENT_USER_ID = "args:user_id";
    private User user;
    private IvorPresenter ivorPresenter;
    private ROLE curRole = ROLE.STD;
    @BindView(R.id.list)
    RecyclerView listView;
    MessageAdapter messages;
    @BindView(R.id.input)
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        long id = getIntent().getLongExtra(INTENT_USER_ID, -1);
        user = App.getStorageAPI().getUser(id);
        ivorPresenter = new IvorPresenter(
                        new Ivor(getResources(),
                        new ActionCall(
                                getString(R.string.cmdCall),
                                x -> {
                                    if (x.isEmpty()) {
                                        List<String> param = ivorPresenter.completeAction();
                                        String dial = "tel:" + param.get(0);
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    } else
                                        appendMessage(new Message(null, x));
                                }),
                        new ActionSendSMS(getString(R.string.cmdSendSMS),
                                x -> {
                                    if (x.isEmpty()) {
                                        List<String> param = ivorPresenter.completeAction();
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(param.get(0), null, param.get(1), null, null);
                                        Toast.makeText(this, R.string.successfulSendSMS, Toast.LENGTH_SHORT).show();
                                    } else
                                        appendMessage(new Message(null, x));
                                }),
                        new ActionSendEmail(getString(R.string.cmdSendEmail), x-> {
                            if (x.isEmpty()) {
                                List<String> param = ivorPresenter.completeAction();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("plain/text");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {param.get(0)});
                                intent.putExtra(Intent.EXTRA_SUBJECT, param.get(1));
                                intent.putExtra(Intent.EXTRA_TEXT, param.get(2));
                                this.startActivity(Intent.createChooser(intent, "Отправка ..."));
                            } else
                                appendMessage(new Message(null, x));
                        }),
                        new ActionSendGPS(getString(R.string.cmdSendGPS), x-> {
                            if (x.isEmpty()) {
                                ivorPresenter.completeAction();
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("geo:0,0?q="+user.city));
                                startActivity(intent);
                            } else
                                appendMessage(new Message(null, x));
                        })
                ),
                this);
        messages = new MessageAdapter(this, new ArrayList<>());
        listView.setAdapter(messages);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) lManager).setStackFromEnd(true);
        listView.setLayoutManager(lManager);
        removeRating();
//        setTitle(user.login + ", " + user.realName);
    }

    public static void start(Context context, long id) {
        Intent msgIntent = new Intent(context, MsgActivity.class);
        msgIntent.putExtra(INTENT_USER_ID, id);
        context.startActivity(msgIntent);
    }


    @OnClick(R.id.buttonSend)
    public void sendClick() {
        messages.append(new Message(user, inputText.getText().toString(), getCurDate()));
        ivorPresenter.clickSend(curRole, inputText.getText().toString());
        inputText.setText("");
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
    }
    @BindView(R.id.buttonDelete)
    ImageButton buttonDelete;

    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
        ivorPresenter.clickDelete(curRole);
    }
    @BindView(R.id.buttonYes)
    ImageButton buttonYes;

    @OnClick(R.id.buttonYes)
    public void clickYes() {
        ivorPresenter.clickEval(1);
    }
    @BindView(R.id.buttonNo)
    ImageButton buttonNo;

    @OnClick(R.id.buttonNo)
    public void clickNo() {
        ivorPresenter.clickEval(-1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ivorPresenter.selectionCommunications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (user.isAdmin())
            getMenuInflater().inflate(R.menu.msg_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        messages.clear();
        switch (item.getItemId()) {
            case R.id.menuModeStd:
                ivorPresenter.setMenuModeStd();
                break;
            case R.id.menuModeIvorAskingKW:
                ivorPresenter.setMenuModeIvorAskingKW();
                break;
            case R.id.menuModeAddKW:
                ivorPresenter.setMenuModeAddKW();
                break;
            case R.id.menuModeAddQuestion:
                ivorPresenter.setMenuModeAddQ();
                break;
            case R.id.menuModeIvorAskingQ:
                ivorPresenter.setMenuModeIvorAskingQ();
                break;
            case R.id.menuSelection:
                ivorPresenter.selectionCommunications();
                break;
            default:
        }
        removeRating();
        return true;
    }

    public enum ROLE {
        STD,
        USER_SEND_ANSWER_FOR_KW,
        USER_SEND_NEW_KW,
        USER_SEND_ANSWER_FOR_Q,
        USER_SEND_NEW_Q,
    }
    /***************************************/
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
        curRole = role;
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
