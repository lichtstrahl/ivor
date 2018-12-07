package root.ivatio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import root.ivatio.bd.users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.App;
import root.ivatio.R;
import root.ivatio.util.StorageAPI;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editAge)
    EditText editAge;
    @BindView(R.id.editCity)
    EditText editCity;
    @BindView(R.id.editLogin)
    EditText editLogin;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.editRetryPassword)
    EditText editRetryPassword;

    @OnClick(R.id.buttonRegister)
    public void registerClick(Button b) {
        if (    editName.getText().toString().equals("") ||
                editEmail.getText().toString().equals("") ||
                editAge.getText().toString().equals("") ||
                editCity.getText().toString().equals("") ||
                editLogin.getText().toString().equals("") ||
                editPassword.getText().toString().equals("") ||
                editRetryPassword.getText().toString().equals("")
                ) {
            Toast.makeText(this, getResources().getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (editRetryPassword.getText().toString().equals(editPassword.getText().toString())) {
            User newUser = User.getUserBuilder()
                    .buildName(editName.getText().toString())
                    .buildEmail(editEmail.getText().toString())
                    .buildAge(Integer.valueOf(editAge.getText().toString()))
                    .buildCity(editCity.getText().toString())
                    .buildLogin(editLogin.getText().toString())
                    .buildPassword(editPassword.getText().toString())
                    .buildTimeEntry()
                    .build();
            StorageAPI.insertUser(newUser);
            finish();
        } else
            Toast.makeText(this, getResources().getString(R.string.nonEqual), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
}
