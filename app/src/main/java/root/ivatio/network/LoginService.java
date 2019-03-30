package root.ivatio.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import root.ivatio.App;
import root.ivatio.activity.LoginActivity;
import root.ivatio.bd.users.User;

public class LoginService extends IntentService {
    private static final String Name = "LoginService";
    private static final String TAG = Name + ": ";
    private static final String INTENT_LOGIN = "args:login";
    private static final String INTENT_PASS = "args:pass";

    public static void start(Context context, String login, String pass) {
        Intent intent = new Intent(context, LoginService.class);
        intent.putExtra(INTENT_LOGIN, login);
        intent.putExtra(INTENT_PASS, pass);
        context.startService(intent);
    }

    public LoginService() {
        super(Name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String login = bundle.getString(INTENT_LOGIN);
                String pass = bundle.getString(INTENT_PASS);
                List<User> users = App.getUserAPI().getUsers().execute().body();
                if (users == null) {
                    LoginActivity.receiveLoginStatus(this, LoginStatus.NULL_BODY);
                } else {
                    for (User u : users) {
                        if (u.login.equals(login) && u.pass.equalsIgnoreCase(pass)) {
                            LoginActivity.receiveLoginStatus(this, LoginStatus.SUCCESSFUL, u);
                            return;
                        }
                    }

                    LoginActivity.receiveLoginStatus(this, LoginStatus.NOT_FOUND);
                    return;
                }
            }
            LoginActivity.receiveLoginStatus(this, LoginStatus.IO_ERROR);
        } catch (IOException e) {
            LoginActivity.receiveLoginStatus(this, LoginStatus.IO_ERROR);
        }
    }
}
