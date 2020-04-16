package x.com.hl_rx.download;

public interface DownloadListener {
    void onDownLoadStart(int progress);

    void onDownLoadCancel();

    void onDownLoadProgress(int progress);

    void onDownLoadError(String error);

    void onDownloadFinish(String filePath);
}
