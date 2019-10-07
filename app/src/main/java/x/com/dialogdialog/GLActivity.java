package x.com.dialogdialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_gl.GLView;

public class GLActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLView(this));
    }
}
