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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
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

public class MsgActivity extends AppCompatActivity implements IvorViewAPI {
    private static final String INTENT_USER = "args:user";
    private User user;
    private IvorPresenter ivorPresenter;
    private ROLE curRole = ROLE.STD;
    private NetworkObserver<ListsHolder> getObserver;
    private NetworkObserver<List> postObserver;
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
        setTitle(user.login + ", " + user.realName);

        getObserver = new NetworkObserver<>(this::successfulLoad, this::errorLoad);
        postObserver = new NetworkObserver<>(this::successfulPost, this::errorPost);
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
        Observable.combineLatest(
                App.getLoadAPI().loadAnswers(),
                App.getLoadAPI().loadCommands(),
                App.getLoadAPI().loadCommunications(),
                App.getLoadAPI().loadCommunicationKeys(),
                App.getLoadAPI().loadKeyWords(),
                App.getLoadAPI().loadQuestions(),
                (lAnswer, lCommand,  lCommunication, lCommunicationKey,  lKeyWord, lQuestion)
                        -> ListsHolder.getBuilder()
                            .buildAnswers(lAnswer)
                            .buildCommands(lCommand)
                            .buildCommunications(lCommunication)
                            .buildCommunicationKeys(lCommunicationKey)
                            .buildKeyWords(lKeyWord)
                            .buildQuestions(lQuestion)
                            .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ivorPresenter.selectionCommunications();
//        ListsHolder newElements = ivorPresenter.getNewElements();
//
//        List<Observable<Command>> observableCommand = new LinkedList<>();
//        for (Command cmd : App.getStorageAPI().getCommands()) {
//            observableCommand.add(
//                    newElements.getCommands().contains(cmd)
//                            ? App.getLoadAPI().insertCommand(cmd.toDTO())
//                            : App.getLoadAPI().replaceCommand(cmd));
//        }
//
//        List<Observable<Answer>> observableAnswer = new LinkedList<>();
//        for (Answer a : App.getStorageAPI().getAnswers()) {
//            observableAnswer.add(
//                    newElements.getAnswers().contains(a)
//                            ? App.getLoadAPI().insertAnswer(a.toDTO())
//                            : App.getLoadAPI().replaceAnswer(a));
//        }
//
//        List<Observable<Communication>> observableCommunication = new LinkedList<>();
//        for (Communication c : App.getStorageAPI().getCommunications()) {
//            observableCommunication.add(newElements.getCommunications().contains(c)
//                    ? App.getLoadAPI().insertCommunication(c.toDTO())
//                    : App.getLoadAPI().replaceCommunication(c));
//        }
//        List<Observable<CommunicationKey>> observableCommunicationKey = new LinkedList<>();
//        for (CommunicationKey c : App.getStorageAPI().getCommunicationKeys()) {
//            observableCommunicationKey.add(newElements.getCommunicationKeys().contains(c)
//                    ? App.getLoadAPI().insertCommunicationKey(c.toDTO())
//                    : App.getLoadAPI().replaceCommunicationKey(c));
//        }
//
//        List<Observable<KeyWord>> observableKeyWord = new LinkedList<>();
//        for (KeyWord w : App.getStorageAPI().getKeyWords()) {
//            observableKeyWord.add(newElements.getKeyWords().contains(w)
//                    ? App.getLoadAPI().insertKeyWord(w.toDTO())
//                    : App.getLoadAPI().replaceKeyWord(w));
//        }
//
//        List<Observable<Question>> observableQuestion = new LinkedList<>();
//        for (Question q : App.getStorageAPI().getQuestions()) {
//            observableQuestion.add(newElements.getQuestions().contains(q)
//                    ? App.getLoadAPI().insertQuestion(q.toDTO())
//                    : App.getLoadAPI().replaceQuestion(q));
//        }
//        // Перебираем сначала в пришедшем списке все Answer-ы, которые будут добавляться.
//        // Далее для каждого такого Answer находим его коммуницации
//        Observable.combineLatest(observableAnswer, Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);
//
//        Observable.combineLatest(observableCommand, Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);
//
//        Observable.combineLatest(observableCommunication, Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);
//
//        Observable.combineLatest(observableCommunicationKey, Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);
//
//        Observable.combineLatest(observableKeyWord, Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);
//
//        Observable.combineLatest(observableQuestion,Arrays::asList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(postObserver);

        getObserver.unsubscribe();
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

    public void successfulLoad(ListsHolder listsHolder) {
        App.getDB().getAnswerDao().deleteAll();
        App.getDB().getCommandDao().deleteAll();
        App.getDB().getCommunicationDao().deleteAll();
        App.getDB().getCommunicationKeyDao().deleteAll();
        App.getDB().getKeyWordDao().deleteAll();
        App.getDB().getQuestionDao().deleteAll();

        // Insert new data
        App.getDB().getAnswerDao().insert(listsHolder.getAnswers());
        App.getDB().getCommandDao().insert(listsHolder.getCommands());
        App.getDB().getCommunicationDao().insert(listsHolder.getCommunications());
        App.getDB().getCommunicationKeyDao().insert(listsHolder.getCommunicationKeys());
        App.getDB().getKeyWordDao().insert(listsHolder.getKeyWords());
        App.getDB().getQuestionDao().insert(listsHolder.getQuestions());

        Toast.makeText(this, R.string.successfulLoading, Toast.LENGTH_SHORT).show();
        progressLoad.setVisibility(View.GONE);
        inputText.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.VISIBLE);
    }

    public void errorLoad(Throwable t) {
        App.logE(t.getMessage());
        Toast.makeText(this, R.string.errorLoading, Toast.LENGTH_SHORT).show();
        progressLoad.setVisibility(View.GONE);
        inputText.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.VISIBLE);
    }

    private void successfulPost(List list) {
        Object o = list.get(0);
        String type = "undefined";
        if (o instanceof Command)
            type = "Command";
        if (o instanceof Answer)
            type = "Answer";
        if (o instanceof  Question)
            type = "Question";
        if (o instanceof Communication)
            type = "Communication";
        if (o instanceof CommunicationKey)
            type = "CommunicationKey";
        if (o instanceof KeyWord)
            type = "KeyWord";


        App.logI(String.format(Locale.ENGLISH, "%s : type %s : size %d", getString(R.string.successfulPost), type, list.size()));
    }

    public void errorPost(Throwable t) {
        App.logE(t.getMessage());
        App.logW(getString(R.string.errorPost));
    }
}
