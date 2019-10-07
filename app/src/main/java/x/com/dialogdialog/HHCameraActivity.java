package x.com.dialogdialog;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_camera.Camera1;

public class HHCameraActivity extends BaseActivity implements View.OnClickListener {
    private Camera1 camera1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        RelativeLayout rl_camera = findViewById(R.id.rl_camera);
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.video).setOnClickListener(this);
        findViewById(R.id.camera).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.switchcamera).setOnClickListener(this);
        camera1 = new Camera1(this, Camera.CameraInfo.CAMERA_FACING_FRONT, rl_camera);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                camera1.startPreView();
                break;
            case R.id.camera:
                break;
            case R.id.video:
                break;
            case R.id.switchcamera:
                camera1.switchCamera();
                break;
            case R.id.stop:
                camera1.stopPreview();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera1 != null) {
            camera1.stopPreview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera1 != null) {
            camera1.startPreView();
        }
    }
}
