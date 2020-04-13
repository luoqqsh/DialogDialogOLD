package x.com.hl_rx.download;

public interface DownloadListener {
    void onStart();

    void onPause();

    void onCancel();

    void onProgress(int progress);
}
