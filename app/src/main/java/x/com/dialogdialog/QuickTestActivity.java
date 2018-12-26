package x.com.dialogdialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.vise.log.ViseLog;

import x.com.hl_tools.hl_assest;
import x.com.hl_tools.hl_file;

public class QuickTestActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String dpath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicktest);
        context = this;
        dpath = hl_file.getDiskCachePath(context) + "/3dtest";
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                hl_assest.copyFilesFassets(context, "3dtest/winter", dpath);
                break;
            case R.id.btn2:
                boolean isex = hl_file.isExist(dpath + "/eye.jpg");
                ViseLog.d(isex);
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
        }
    }
}
