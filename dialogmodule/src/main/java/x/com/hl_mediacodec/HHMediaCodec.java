package x.com.hl_mediacodec;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import x.com.hl_log.ViseLog;

public class HHMediaCodec {
    private SurfaceView surfaceView;
    private MediaCodec mediaCodec;
    private DataInputStream mInputStream;

    public void init(Context context, ViewGroup viewGroup) throws IOException {

        ViseLog.d("init");
        //解码
        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_MPEG4);

        surfaceView = new SurfaceView(context);
        viewGroup.addView(surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                ViseLog.d("surfaceCreated");
                MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_MPEG4, 720, 1280);
                mediaCodec.configure(mediaFormat, surfaceView.getHolder().getSurface(), null, 0);
                mediaCodec.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    public void getFileInputStream(String fileName) {
        try {
            File file = new File(fileName);
            mInputStream = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                mInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
