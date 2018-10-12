package x.com.dialogdialog;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import x.com.hl_dialog.DFLoadingDialog;

public class BaseActivity extends AppCompatActivity {
    public DFLoadingDialog a;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DialogUtil.createLoadDialog(this);
         a = new DFLoadingDialog();
    }
}
