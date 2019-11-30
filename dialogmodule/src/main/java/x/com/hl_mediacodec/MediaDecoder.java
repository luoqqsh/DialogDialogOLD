package x.com.hl_mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.File;
import java.io.IOException;

import x.com.hl_base.hl_interface.HOnListener;

public class MediaDecoder extends Thread {

    protected String mVideoFilePath = null;

    public static final long TIME_US = 10000;

    protected MediaExtractor mExtractor = null;
    protected MediaCodec mDecoder = null;
    protected MediaFormat mediaFormat;
    protected HOnListener<String> mCallback;
    protected Surface mSurface;

    public MediaDecoder(String videoFilePath, Surface surface, HOnListener<String> callback) {
        this.mVideoFilePath = videoFilePath;
        this.mSurface = surface;
        this.mCallback = callback;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        prepare();
    }

    public void prepare() {
        try {
            File videoFile = new File(mVideoFilePath);
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(videoFile.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
