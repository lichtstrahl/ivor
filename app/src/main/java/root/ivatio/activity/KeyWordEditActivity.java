package root.ivatio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import bd.Answer.Answer;
import bd.CommunicationKey.CommunicationKey;
import bd.KeyWord.KeyWord;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import root.ivatio.App;
import root.ivatio.R;

public class KeyWordEditActivity extends AppCompatActivity {
    @BindView(R.id.listKeyWord)
    ListView listKeyWord;
    ArrayAdapter<String> adapterK;
    @BindView(R.id.viewKeyWord)
    TextView viewKeyWord;
    @OnItemClick(R.id.listKeyWord)
    public void itemKeyWordClick(int pos) {
        viewKeyWord.setText(adapterK.getItem(pos));
        enabledButtonConnect();
    }

    @BindView(R.id.listAnswer)
    ListView listAnswer;
    ArrayAdapter<String> adapterA;
    @BindView(R.id.viewAnswer)
    TextView viewAnswer;
    @OnItemClick(R.id.listAnswer)
    public void itemAnswerClick(int pos) {
        viewAnswer.setText(adapterA.getItem(pos));
        enabledButtonConnect();
    }


    @BindView(R.id.buttonConnect)
    Button buttonConnect;
    @OnClick(R.id.buttonConnect)
    public void clickConnect() {
        long kID = App.getDB().getKeyWordDao().getWordID(viewKeyWord.getText().toString());
        long aID = App.getDB().getAnswerDao().getAnswerID(viewAnswer.getText().toString());
        App.getDB().getCommunicationKeyDao().insert(new CommunicationKey(kID, aID));
        viewKeyWord.clearComposingText();
        viewAnswer.clearComposingText();
        enabledButtonConnect();
    }

    @BindView(R.id.editKeyWord)
    EditText editKeyWord;
    @OnTextChanged(R.id.editKeyWord)
    public void textChanged(CharSequence text) {
        buttonAppend.setEnabled(text.length() != 0);
    }
    @BindView(R.id.buttonAppend)
    Button buttonAppend;
    @OnClick(R.id.buttonAppend)
    public void clickButtonAppend() {
        App.getDB().getKeyWordDao().insert(new KeyWord(editKeyWord.getText().toString()));
        editKeyWord.setText("");
    }

    @OnFocusChange(R.id.editKeyWord)
    public void focusChange(){
        ScrollView sv = findViewById(R.id.scrollView);
        sv.scrollTo(0, sv.getBottom());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_word_edit);
        ButterKnife.bind(this);

        adapterA = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapterK = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listAnswer.setAdapter(adapterA);
        listKeyWord.setAdapter(adapterK);

        updateListAnswer();
        updateListKeyWord();

        buttonConnect.setEnabled(false);
    }

    private void updateListKeyWord() {
        List<Answer> list = App.getDB().getAnswerDao().getAll();

        for (Answer a: list)
            adapterA.add(a.content);
    }

    private void updateListAnswer() {
        List<KeyWord> list = App.getDB().getKeyWordDao().getAll();

        for (KeyWord word: list)
            adapterK.add(word.content);
    }

    private void enabledButtonConnect() {
        buttonConnect.setEnabled(viewAnswer.getText().length() * viewKeyWord.getText().length() != 0);
    }
}
