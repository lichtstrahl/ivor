package root.ivatio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.bd.users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.App;
import root.ivatio.R;
import root.ivatio.network.UserPostObserver;

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
    @BindView(R.id.progressRegister)
    ProgressBar progressRegister;

    private UserPostObserver userPostObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        userPostObserver = new UserPostObserver(
                user -> {
                    progressRegister.setVisibility(View.GONE);
                    Toast.makeText(this, R.string.userSuccessfulAppend, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    progressRegister.setVisibility(View.GONE);
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    App.logE(error.getMessage());
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        userPostObserver.unsubscribe();
    }

    @OnClick(R.id.buttonRegister)
    public void registerClick() {
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
            progressRegister.setVisibility(View.VISIBLE);
            User newUser = User.getUserBuilder()
                    .buildName(editName.getText().toString())
                    .buildEmail(editEmail.getText().toString())
                    .buildAge(Integer.valueOf(editAge.getText().toString()))
                    .buildCity(editCity.getText().toString())
                    .buildLogin(editLogin.getText().toString())
                    .buildPassword(editPassword.getText().toString())
                    .buildTimeEntry()
                    .build();
            App.getUserAPI().postUser(new PostUser(newUser))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userPostObserver);

        } else
            Toast.makeText(this, getResources().getString(R.string.nonEqual), Toast.LENGTH_SHORT).show();
    }

    public class PostUser {
        @SerializedName("realName")
        public String realName;
        @SerializedName("login")
        public String login;
        @SerializedName("pass")
        public String pass;
        @SerializedName("age")
        public Integer age;
        @SerializedName("city")
        public String city;
        @SerializedName("email")
        public String email;
        @SerializedName("lastEntry")
        public String lastEntry;
        @SerializedName("admin")
        public Integer admin;

        PostUser(User user) {
            this.admin = user.admin;
            this.age = user.age;
            this.city = user.city;
            this.email = user.email;
            this.lastEntry = user.lastEntry;
            this.login = user.login;
            this.pass = user.pass;
            this.realName = user.realName;
        }
    }
}
