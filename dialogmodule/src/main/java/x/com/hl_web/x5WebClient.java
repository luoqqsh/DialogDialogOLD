package x.com.hl_web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;

import x.com.hl_log.ViseLog;

public class x5WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {

        if (!url.startsWith("http")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            if (isIntentExisting(webView.getContext(), intent)) {
                webView.getContext().startActivity(intent);
            }
        } else {
            webView.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
        super.onPageStarted(webView, url, bitmap);
//        ViseLog.d(url);
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
//        ViseLog.d(url);
    }

    //每一个资源（比如图片）的加载都会调用一次。
    @Override
    public void onLoadResource(WebView webView, String url) {
        super.onLoadResource(webView, url);
//                ViseLog.d(url);
    }

    //请求错误处理。
    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        super.onReceivedError(webView, errorCode, description, failingUrl);
//        ViseLog.d(failingUrl);
//        ViseLog.d(errorCode);
//        ViseLog.d(description);
    }

    //处理https请求
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();    //表示等待证书响应
        // handler.cancel();      //表示挂起连接，为默认方式
        // handler.handleMessage(null);    //可做其他处理

    }

    private boolean isIntentExisting(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return resolveInfo.size() > 0;
    }
}
