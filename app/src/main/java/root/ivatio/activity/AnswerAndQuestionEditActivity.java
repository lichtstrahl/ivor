package root.ivatio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import bd.answer.Answer;
import bd.communication.Communication;
import bd.qustion.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import root.ivatio.App;
import root.ivatio.R;

public class AnswerAndQuestionEditActivity extends AppCompatActivity {
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    // ---------------------------------------------------------
    //                      QUESTION
    // ---------------------------------------------------------
    @BindView(R.id.viewQuestion)
    TextView viewQuestion;
    @BindView(R.id.listQuestion)
    ListView listQuestion;
    @OnItemClick(R.id.listQuestion)
    public void clickQuestion(int pos) {
        viewQuestion.setText(adapterQ.getItem(pos));
    }
    private ArrayAdapter<String> adapterQ;

    @BindView(R.id.editQuestion)
    EditText editQuestion;
    @BindView(R.id.editAnswer)
    EditText editAnswer;

    // TODO Возможно здесь не стоит делать постоянные обращения к БД
    @OnClick(R.id.buttonAppendQuestion)
    public void appendQuestion() {
        String string = editQuestion.getText().toString();
        if (!string.isEmpty()) {
            App.getDB().getQuestionDao().insert(new Question(string));
            updateQuestion();
            editQuestion.setText("");
        }
    }
    @OnFocusChange(R.id.editQuestion)
    public void focusChangeQuestion() {
        focusChangeEdit();
    }

    // ---------------------------------------------------------
    //                       ANSWER
    // ---------------------------------------------------------
    @BindView(R.id.viewAnswer)
    TextView viewAnswer;
    @BindView(R.id.listAnswer)
    ListView listAnswer;
    @OnItemClick(R.id.listAnswer)
    public void clickAnswer(int pos) {
        viewAnswer.setText(adapterA.getItem(pos));
    }
    private ArrayAdapter<String> adapterA;

    // TODO Аналогично пункту выше
    @OnClick(R.id.buttonAppendAnswer)
    public void appendAnswer() {
        String string = editAnswer.getText().toString();
        if (!string.isEmpty()) {
            App.getDB().getAnswerDao().insert(new Answer(string));
            updateAnswer();
            editAnswer.setText("");
        }
    }
    @OnFocusChange(R.id.editAnswer)
    public void focusChangeAnswer() {
        focusChangeEdit();
    }

    @OnClick(R.id.buttonConnect)
    public void clickConnect() {
        Question q = App.getDB().getQuestionDao().getQuestion(viewQuestion.getText().toString());
        Answer a = App.getDB().getAnswerDao().getAnswer(viewAnswer.getText().toString());

        if (a != null && q != null) {
            App.getDB().getCommunicationDao().insert(new Communication(q.id, a.id, 0));
            viewAnswer.setText("");
            viewQuestion.setText("");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_and_question_edit);
        ButterKnife.bind(this);

        adapterQ = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapterA = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listQuestion.setAdapter(adapterQ);
        listAnswer.setAdapter(adapterA);

        updateAnswer();
        updateQuestion();
    }

    private void updateQuestion() {
        List<Question> list = App.getDB().getQuestionDao().getAll();

        adapterQ.clear();
        for (Question q : list)
            adapterQ.add(q.content);
        adapterQ.notifyDataSetChanged();
    }

    private void updateAnswer() {
        List<Answer> list = App.getDB().getAnswerDao().getAll();

        adapterA.clear();
        for (Answer a : list)
            adapterA.add(a.content);
        adapterA.notifyDataSetChanged();
    }

    private void focusChangeEdit() {
//        scrollView.scrollTo(0, scrollView.getBottom());
//        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
