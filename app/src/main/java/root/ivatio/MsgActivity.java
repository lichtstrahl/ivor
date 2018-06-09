package root.ivatio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import Users.User;

public class MsgActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        user = App.getDB().getUserDao().getUser(getIntent().getLongExtra(App.USER_INDEX, App.SUPER_USER_INDEX));
        ((TextView)findViewById(R.id.personName)).setText(user.realName);
        ((TextView)findViewById(R.id.personAge)).setText(String.valueOf(user.age));
    }
}
