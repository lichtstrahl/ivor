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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.MessageAdapter;
import root.ivatio.R;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorPresenter;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.ivor.action.ActionCall;
import root.ivatio.ivor.action.ActionSendEmail;
import root.ivatio.ivor.action.ActionSendGPS;
import root.ivatio.ivor.action.ActionSendSMS;
import root.ivatio.network.NetworkObserver;
import root.ivatio.util.ListsHolder;
import root.ivatio.util.ROLE;

public class MsgActivity extends AppCompatActivity implements IvorViewAPI {
    private static final String INTENT_USER = "args:user";
    private User user;
    private IvorPresenter ivorPresenter;
    private ROLE curRole = ROLE.STD;
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
        user = (User)getIntent().getSerializableExtra(INTENT_USER);

        ivorPresenter = new IvorPresenter(
                new Ivor(
                        getResources(),
                        App.getStorageAPI(),
                        new ActionCall("позвонить", x -> {
                            if (x.isEmpty()) {
                                List<String> param = ivorPresenter.completeAction();
                                String dial = "tel:" + param.get(0);
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }
                        })),
                this);

        messages = new MessageAdapter(this, new ArrayList<>());
        listView.setAdapter(messages);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) lManager).setStackFromEnd(true);
        listView.setLayoutManager(lManager);
        removeRating();
        setTitle(user.login + ", " + user.realName);

        initialDialogInterface();
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
        messages.append(new Message(user, inputText.getText().toString(), getCurDate()));
        ivorPresenter.clickSend(curRole, inputText.getText().toString());
        inputText.setText("");
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
    }
    @OnClick(R.id.buttonDelete)
    public void clickDelete() {
        ivorPresenter.clickDelete(curRole);
        listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
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
    protected void onStart() {
        super.onStart();
        // Скачивание данных их сети
//        Observable.combineLatest(
//                App.getLoadAPI().loadAnswers(),
//                App.getLoadAPI().loadCommands(),
//                App.getLoadAPI().loadCommunications(),
//                App.getLoadAPI().loadCommunicationKeys(),
//                App.getLoadAPI().loadKeyWords(),
//                App.getLoadAPI().loadQuestions(),
//                (lAnswer, lCommand,  lCommunication, lCommunicationKey,  lKeyWord, lQuestion)
//                        -> ListsHolder.getBuilder()
//                            .buildAnswers(lAnswer)
//                            .buildCommands(lCommand)
//                            .buildCommunications(lCommunication)
//                            .buildCommunicationKeys(lCommunicationKey)
//                            .buildKeyWords(lKeyWord)
//                            .buildQuestions(lQuestion)
//                            .build())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ivorPresenter.onStop();
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
                ivorPresenter.selectionForce();
                break;
            default:
        }
        removeRating();
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
    public void appendMessage(@Nullable Message msg) {
        if (msg != null) {
            messages.append(msg);
            listView.smoothScrollToPosition(listView.getAdapter().getItemCount());
        }
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

    @Override
    public void switchProgress(int state) {
        progressLoad.setVisibility(state);
    }

    public void initialDialogInterface() {
        progressLoad.setVisibility(View.GONE);
        inputText.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.VISIBLE);
    }
}
