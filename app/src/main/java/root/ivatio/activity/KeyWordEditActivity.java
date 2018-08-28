package root.ivatio.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import bd.Answer.Answer;
import bd.KeyWord.KeyWord;
import butterknife.BindView;
import butterknife.ButterKnife;
import root.ivatio.App;
import root.ivatio.R;

public class KeyWordEditActivity extends AppCompatActivity {

    @BindView(R.id.listAnswer)
    ListView listAnswer;
    @BindView(R.id.editContentKey)
    EditText editContentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_word_edit);
        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (Answer a : App.getDB().getAnswerDao().getAll())
            adapter.add(a.content);
        listAnswer.setAdapter(adapter);
        listAnswer.setOnItemClickListener(new ItemAnswerClickListener(adapter, editContentKey, this));

    }
}

class ItemAnswerClickListener implements AdapterView.OnItemClickListener {
    private ArrayAdapter adapter;
    private EditText edit;
    private Context context;
    ItemAnswerClickListener(ArrayAdapter adapter, EditText e, Context context) {
        this.adapter = adapter;
        edit = e;
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!edit.getText().toString().isEmpty()) {
            String s = adapter.getItem(i).toString();
            App.getDB().getKeyWordDao().insert(new KeyWord(edit.getText().toString(), App.getDB().getAnswerDao().getId(s)));
            edit.setText("");
        }
    }
}