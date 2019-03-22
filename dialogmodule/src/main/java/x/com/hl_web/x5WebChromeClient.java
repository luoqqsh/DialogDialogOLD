package x.com.hl_web;

import android.util.Log;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

public class x5WebChromeClient extends WebChromeClient {
    //获得网页加载进度
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
//        ViseLog.d(newProgress);
    }

    //获得网页中的标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        Log.i("hl-x5web", "title:" + title);
    }

    //支持javascript的警告框
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//        ViseLog.d(message);
//        ViseLog.d(result);
        return true;
    }

    //支持javascript的确认框
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//        ViseLog.d(message);
//        ViseLog.d(result);
        // 返回布尔值：判断点击时确认还是取消。true表示点击了确认；false表示点击了取消；
        return true;
    }

    //支持javascript输入框
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        return true;
    }

    /**
     * 播放网络视频时进入全屏
     */
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {

    }

    /**
     * 视频播放退出全屏会被调用的
     */
    @Override
    public void onHideCustomView() {

    }

    /**
     * 播放进度条
     */
    @Override
    public View getVideoLoadingProgressView() {
        return null;
    }
}
