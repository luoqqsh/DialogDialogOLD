package x.com.hl_rx.download;

public interface DownloadListener {
    //由于支持断点续传，刚开始的进度不一定是0
    void onDownLoadStart(int startProgress);

    void onDownLoadCancel();

    void onDownLoadProgress(int progress);

    void onDownLoadError(String error);

    void onDownloadFinish(String filePath);
}
