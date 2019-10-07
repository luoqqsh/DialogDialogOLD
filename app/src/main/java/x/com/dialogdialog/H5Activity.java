package x.com.dialogdialog;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_tools.hl_permissions;
import x.com.hl_web.x5WebChromeClient;
import x.com.hl_web.x5WebClient;
import x.com.hl_web.x5WebDownloadListener;
import x.com.hl_web.x5WebView;

public class H5Activity extends BaseActivity {
    private x5WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mLayout = new LinearLayout(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new x5WebView(this);
        webView.setLayoutParams(params);
        mLayout.addView(webView);
        setContentView(mLayout);
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
        hl_permissions.applyPermission(this, permission, null);
//        x5Manager.getInstance().init(this);
        webView.initWebSetting();
        webView.setWebViewClient(new x5WebClient());
        webView.setWebChromeClient(new x5WebChromeClient());
        webView.addJavascriptInterface(new JavaScriptInterface(), "JSInterface");
        webView.setDownloadListener(new x5WebDownloadListener());
        String encode = null;
        try {
            encode = URLEncoder.encode("", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String s11 = "author=" + encode;
        String[] cookiess = new String[]{s11};
        webView.setCookies(".3500.com", cookiess);
        webView.loadUrl("https://www.iqiyi.com/");
    }

    private void clearCache() {
        webView.clearCache(true);//清除缓存
        webView.clearHistory();//清除历史记录
        webView.clearFormData();//清除表单数据
    }

    @Override
    protected void onDestroy() {
//        InputMethodManagerUtils.fixInputMethodManagerLeak(this);
        try {
            if (webView != null) {
                webView.destroyWebView();
                webView = null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            super.onDestroy();
        }
    }

    private void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    private void goForwardPage() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        webView.pauseTimers();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        webView.resumeTimers();
        webView.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBackPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 安卓调用js方法2
    // webView.addJavascriptInterface(new JavaScriptInterface(), "JSInterface");
    public class JavaScriptInterface {
        @JavascriptInterface
        public void hello(String msg) {
        }
        //...
    }


}