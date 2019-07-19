package x.com.hl_mediacodec;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_file;

public class Codec {
    private ExecutorService exec;
    private static Codec codec;
    private String outputFile = "/storage/emulated/0/Dialog_MediaCodec";

    public static Codec instance() {
        if (codec == null) {
            codec = new Codec();
        }
        return codec;
    }

    public void videos(String inputfile) {
        if (!hl_file.isFileExist(outputFile)) {
            ViseLog.d(hl_file.createDir(outputFile));
        }
        exec = Executors.newCachedThreadPool();
        exec.execute(new Runnable() {
            @Override
            public void run() {

            }
        });


    }
}
