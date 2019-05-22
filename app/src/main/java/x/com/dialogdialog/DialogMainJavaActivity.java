package x.com.dialogdialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_dialog.DFNDialog;
import x.com.hl_tools.hl_drawable;
import x.com.hl_tools.hl_img;
import x.com.hl_tools.hl_orientation;

public class DialogMainJavaActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.color.brown);
        init();
        test();
    }

    private void test() {
        ImageView imageView = findViewById(R.id.imgv_test);
        imageView.setBackground(hl_drawable.getRoundRectDrawable(0, hl_img.getColorFromString("#5bc0de"), true, 10));
    }

    private void init() {
        findViewById(R.id.java).setVisibility(View.GONE);
        findViewById(R.id.tongzhi).setOnClickListener(view -> startActivity(new Intent(mContext, NotificationActivity.class)));
        findViewById(R.id.media).setOnClickListener(view -> startActivity(new Intent(mContext, HHMediaActivity.class)));
        findViewById(R.id.x5web).setOnClickListener(v -> startActivity(new Intent(mContext, H5Activity.class)));
        findViewById(R.id.log).setOnClickListener(v -> startActivity(new Intent(mContext, LogActivity.class)));
        findViewById(R.id.open).setOnClickListener(v -> startActivity(new Intent(mContext, GLActivity.class)));
        findViewById(R.id.close).setOnClickListener(v -> {
            hl_orientation orientation = new hl_orientation();
            orientation.orientationEnable(this, null);
        });

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFNDialog b = DFNDialog.newInstance();
                b.setMessage("dddddddddd", 4);
                if (!b.isAdded()) {
                    b.show(getSupportFragmentManager(), "normalDialog");
                }
            }
        });
    }
}
