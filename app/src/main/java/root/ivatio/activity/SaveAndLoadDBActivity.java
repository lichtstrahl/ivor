package root.ivatio.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import Dialog.OpenFileDialog;
import Dialog.SetDirDialog;
import bd.BDCutodian;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.ivatio.R;

public class SaveAndLoadDBActivity extends AppCompatActivity {
    @BindView(R.id.editFileName)
    EditText editFileName;

    @OnClick(R.id.buttonSaveDB)
    public void clickSaveDB(ImageButton button) {
        if (!editFileName.getText().toString().isEmpty()) {
            SetDirDialog dialog = new SetDirDialog(this);
            dialog.setOpenDialogListener(new DialogSaveListener(this, editFileName.getText().toString()));
            dialog.show();
        } else
            Toast.makeText(this, getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonLoadDB)
    public void clickLoadDB(ImageButton button) {
        OpenFileDialog dialog = new OpenFileDialog(this);
        dialog.setOpenDialogListener(new DialogLoadListener(this));
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_and_load_db);
        ButterKnife.bind(this);
    }
}

class DialogSaveListener implements OpenFileDialog.OpenDialogListener {
    private Context context;
    private String fileName;

    DialogSaveListener(Context c, String name) {
        context = c;
        fileName = name;
    }
    @Override
    public void OnSelectedFile(String path) {
        BDCutodian.saveDB(context, path+"/"+fileName);
    }
}

class DialogLoadListener implements OpenFileDialog.OpenDialogListener {
    private Context context;
    DialogLoadListener(Context c) {
        context = c;
    }
    @Override
    public void OnSelectedFile(String fileName) {
        BDCutodian.loadDB(context, fileName);
    }
}