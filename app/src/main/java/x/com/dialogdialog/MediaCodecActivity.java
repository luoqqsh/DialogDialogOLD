package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_log.ViseLog;
import x.com.hl_mediacodec.VideoDecoder;

public class MediaCodecActivity extends BaseActivity {
    private String musicPath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3";
    private String videoPath = "/storage/emulated/0/DCIM/Camera/19920112735275.mp4";
    private TextView tv_path;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediacodec);
        tv_path = findViewById(R.id.path);
        tv_path.setText(videoPath);

        VideoDecoder videoDecoder=new VideoDecoder(30, 30, 720, 1280, new VideoDecoder.VideoEncodeCallback() {
            @Override
            public void onEncodeData(ByteBuffer buffer, int keyFrame) {
                ViseLog.d(buffer);
                ViseLog.d(keyFrame);
            }
        });
        videoDecoder.startEncode();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
