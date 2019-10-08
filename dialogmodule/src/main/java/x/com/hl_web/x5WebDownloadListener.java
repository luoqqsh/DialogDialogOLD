package x.com.hl_web;

import com.tencent.smtt.sdk.DownloadListener;

import x.com.hl_log.ViseLog;

public class x5WebDownloadListener implements DownloadListener {
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        ViseLog.d("hl-x5web", "下载文件：" + url);
//        ViseLog.d(url);
//        ViseLog.d(userAgent);
//        ViseLog.d(contentDisposition);
//        ViseLog.d(mimetype);
//        ViseLog.d(contentLength);
    }
}
