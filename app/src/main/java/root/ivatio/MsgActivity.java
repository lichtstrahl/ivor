package root.ivatio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import BD.Users.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgActivity extends AppCompatActivity {

    private User user;

    @BindView(R.id.list)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);

        user = App.getDB().getUserDao().getUser(getIntent().getLongExtra(App.USER_INDEX, -1));
        ((TextView)findViewById(R.id.personName)).setText(user.realName);
        ((TextView)findViewById(R.id.personAge)).setText(String.valueOf(user.age));

        ArrayList<Message> data = new ArrayList<>();
        data.add(new Message(user, "Привет", Calendar.getInstance().getTime()));
        data.add(new Message(user, "Привет)", Calendar.getInstance().getTime()));
        listView.setAdapter(new MessageAdapter(this, data));
    }
}
