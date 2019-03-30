package root.ivatio.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.App;
import root.ivatio.R;
import root.ivatio.bd.users.User;
import root.ivatio.network.LoginService;
import root.ivatio.network.LoginStatus;

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

    private LoginReceiver loginReceiver = new LoginReceiver();

    @OnClick(R.id.buttonLogin)
    public void loginClick() {
        progressLogin.setVisibility(View.VISIBLE);
        LoginService.start(this, editLogin.getText().toString(), editPassword.getText().toString());
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(loginReceiver, new IntentFilter(ACTION_LOGIN));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(loginReceiver);
    }

    class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LoginStatus status = (LoginStatus)intent.getSerializableExtra(INTENT_STATUS);
            switch (status) {
                case SUCCESSFUL:
                    long id = intent.getLongExtra(INTENT_ID, -1);
                    User u = (User)intent.getSerializableExtra(INTENT_USER);
                    MsgActivity.start(LoginActivity.this, u);
                    break;

                case NOT_FOUND:
                    Toast.makeText(LoginActivity.this, R.string.USER_NOT_FOUND, Toast.LENGTH_SHORT).show();
                    App.logI(getString(R.string.USER_NOT_FOUND));
                    break;

                case IO_ERROR:
                    Toast.makeText(LoginActivity.this, R.string.IO_EXCEPTION_RESPONSE, Toast.LENGTH_SHORT).show();
                    App.logE(getString(R.string.IO_EXCEPTION_RESPONSE));
                    break;

                case NULL_BODY:
                    Toast.makeText(LoginActivity.this, R.string.NUll_BODY, Toast.LENGTH_SHORT).show();
                    App.logW(getString(R.string.NUll_BODY));
                    break;
            }
            progressLogin.setVisibility(View.GONE);
        }
    }

    public static void receiveLoginStatus(Service service, LoginStatus status) {
        Intent intent = new Intent().setAction(ACTION_LOGIN);
        intent.putExtra(INTENT_STATUS, status);
        service.sendBroadcast(intent);
    }

    public static void receiveLoginStatus(Service service, LoginStatus status, User u) {
        Intent intent = new Intent().setAction(ACTION_LOGIN);
        intent.putExtra(INTENT_STATUS, status);
        intent.putExtra(INTENT_USER, u);
        service.sendBroadcast(intent);
    }
}
