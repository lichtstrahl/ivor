package root.ivatio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String VAR_X = "x";
    private final String PREF_NAME = "ivor";
    private int x = 0;

    @BindView(R.id.viewX)
    TextView viewX;

    @BindView(R.id.listView)
    ListView listView;

    @OnClick(R.id.btn)
    public void inc(Button b) {
        setViewX(++x);
    }

    @OnClick(R.id.buttonInsert)
    public void insertClick(Button b) {
        Employee e = new Employee();
        e.id = 1;
        e.name = "Igor";
        e.salary = 1000;
        App.getDB().employeeDao().insert(e);
    }

    @OnClick(R.id.buttonSelectAll)
    public void selectAllClick(Button b) {
        List<Employee> list = App.getDB().employeeDao().getAll();
        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        adapter.clear();
        for (Employee e : list)
            adapter.add(e.name);
        Toast.makeText(this, this.getDatabasePath(App.getDBName()).getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    private void setViewX(int x) {
        viewX.setText(String.format(Locale.ENGLISH, "%d", x));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        setViewX(x);

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putInt(VAR_X, x).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        x = getSharedPreferences(PREF_NAME, MODE_PRIVATE).getInt(VAR_X, 0);
        setViewX(x);
    }

}
