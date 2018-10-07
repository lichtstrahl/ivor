package root.ivatio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import root.ivatio.App;
import root.ivatio.R;

public class SelectTable extends AppCompatActivity {
    @BindView(R.id.listTables)
    ListView listTables;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        List<String> list = App.getTables();
        for (String s : list)
            adapter.add(s);
        listTables.setAdapter(adapter);
        listTables.setOnItemClickListener(new ItemClickListener(this, adapter));
    }
}

class ItemClickListener implements AdapterView.OnItemClickListener {
    private ArrayAdapter adapter;
    private Context context;

    ItemClickListener(Context context, ArrayAdapter a) {
        this.adapter = a;
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tableName = (String)adapter.getItem(position);
        if (tableName.equals("KeyWord") || tableName.equals("CommunicationKey")) {
            Intent intent = new Intent(context, KeyWordEditActivity.class);
            context.startActivity(intent);
        } else if (tableName.equals("Question") || tableName.equals("Answer") || tableName.equals("Communication")) {
            Intent intent = new Intent(context, AnswerAndQuestionEditActivity.class);
            context.startActivity(intent);
        } else
            Toast.makeText(context, context.getResources().getString(R.string.tableNotFound), Toast.LENGTH_SHORT).show();
    }
}