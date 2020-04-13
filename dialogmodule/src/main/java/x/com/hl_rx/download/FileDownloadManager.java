package x.com.hl_rx.download;

import android.content.Context;

public class FileDownloadManager {
    public static FileDownloadManager fileDownloadManager;
    public DownloadListener downloadListener;

    public static FileDownloadManager getInstance(Context context) {
        if (fileDownloadManager == null) {
            fileDownloadManager = new FileDownloadManager();
        }
        return fileDownloadManager;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
}
