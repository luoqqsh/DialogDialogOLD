package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_gl.GLView;

public class GLActivity extends BaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    public View getLayoutView() {
        return new GLView(this);
    }
}
