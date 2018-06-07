package root.ivatio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.List;
import java.util.Locale;

import Answer.Answer;
import Qustion.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.listView)
    ListView listView;

    @OnClick(R.id.buttonInsert)
    public void insertClick(Button b) {
        Question q = new Question();
        q.content = "Ololo";
        q.power = 0;
        App.getDB().getQuestionDao().insert(q);
    }

    @OnClick(R.id.buttonSelectAll)
    public void selectAllClick(Button b) {
        List<Question> list = App.getDB().getQuestionDao().getAll();

        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        adapter.clear();
        for (Question q: list)
            adapter.add(q.content);
    }

    @OnClick(R.id.buttonDeleteAll)
    public void deleteAllClick(Button b) {
        App.getDB().getQuestionDao().deleteAll();
        selectAllClick(null);
    }

    @OnClick(R.id.buttonInsertAnswer)
    public void insertAnswer(Button b) {
        Answer a = new Answer();
        a.content = "ответ";
        a.power = 1;

        App.getDB().getAnswerDao().insert(a);
    }

    @OnClick(R.id.buttonSelectAllAnswer)
    public void selectAllAnswer(Button b) {
        List<Answer> list = App.getDB().getAnswerDao().getAll();

        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        adapter.clear();
        for (Answer a : list)
            adapter.add(a.content);
    }

    @OnClick(R.id.buttonDeleteAllAnswer)
    public void deleteAllAnswer(Button b) {
        App.getDB().getAnswerDao().deleteAll();
        selectAllAnswer(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item));
    }
}
