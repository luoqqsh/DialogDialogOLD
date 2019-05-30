package x.com.dialogdialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_player.HHPlayerView3;

public class HHMediaViewActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new HHPlayerView3(this));
    }
}
