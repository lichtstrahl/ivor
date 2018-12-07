package root.ivatio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.App;
import root.ivatio.R;
import root.ivatio.bd.users.User;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.editLogin)
    EditText editLogin;
    @BindView(R.id.editPassword)
    EditText editPassword;

    @OnClick(R.id.buttonLogin)
    public void loginClick(Button b) {
        User user = App.getStorageAPI().getUser(editLogin.getText().toString(), editPassword.getText().toString());
        if (user == null)
            Toast.makeText(this, "Пользователь не найден или пароль неверен", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, MsgActivity.class);
            intent.putExtra(App.USER_INDEX, user.id);
            startActivity(intent);
        }
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
}
