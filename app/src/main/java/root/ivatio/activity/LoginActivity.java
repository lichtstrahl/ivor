package root.ivatio.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.app.App;
import root.ivatio.R;
import root.ivatio.activity.msg.MsgActivity;
import root.ivatio.bd.users.User;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

public class LoginActivity extends AppCompatActivity {
    private static final String ACTION_LOGIN = "root.ivatio.LOGIN";
    private static final String INTENT_STATUS = "args:Status";
    private static final String INTENT_ID = "args:id";
    private static final String INTENT_USER = "args:user";

    @BindView(R.id.editLogin)
    EditText editLogin;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.progressLogin)
    ProgressBar progressLogin;
    private SingleNetworkObserver<ServerAnswerDTO<User>> loginObserver;

    @OnClick(R.id.buttonLogin)
    public void loginClick() {
        progressLogin.setVisibility(View.VISIBLE);

        String login = editLogin.getText().toString();
        String pass = editPassword.getText().toString();

        App.getServerAPI().login(User.getLoginUser(login, pass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginObserver);

    }

    @OnClick(R.id.buttonRegister)
    public void registerClick(Button b) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginObserver = new SingleNetworkObserver<>(this::networkSuccessful, this::networkError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginObserver.unsubscribe();
    }

    private void networkSuccessful(ServerAnswerDTO<User> answer) {
        progressLogin.setVisibility(View.GONE);
        MsgActivity.start(this, answer.getData());
    }

    private void networkError(Throwable t) {
        progressLogin.setVisibility(View.GONE);
        App.logE(t.getMessage());
    }
}
