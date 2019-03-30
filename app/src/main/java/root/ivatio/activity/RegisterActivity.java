package root.ivatio.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.App;
import root.ivatio.R;
import root.ivatio.bd.users.User;
import root.ivatio.network.dto.ServerAnswerDTO;
import root.ivatio.network.observer.SingleNetworkObserver;

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
    private SingleNetworkObserver<ServerAnswerDTO> userPostObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        userPostObserver = new SingleNetworkObserver<>(
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

            App.getServerAPI().register(newUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userPostObserver);

        } else
            Toast.makeText(this, getResources().getString(R.string.nonEqual), Toast.LENGTH_SHORT).show();
    }
}
