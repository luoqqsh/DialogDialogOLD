package x.com.dialogdialog;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.DownloadListener;
import com.vise.log.ViseLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import x.com.hl_tools.hl_permissions;
import x.com.hl_web.x5WebView;

public class H5Activity extends AppCompatActivity {
    private x5WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        webView = new x5WebView(this);
        LinearLayout mLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new x5WebView(this);
        webView.setLayoutParams(params);
        mLayout.addView(webView);

        setContentView(mLayout);
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_FINE_LOCATION};
        hl_permissions.applyPermission(this, permission, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {

            }

            @Override
            public void requestPermissionFail(boolean isNoTip) {

            }
        });
//        TbsManager.getInstance().init(this);
        webView.initWebSetting();
        webView.initWebClient();
        webView.initWebChromeClient();
        webView.addJavascriptInterface(new JavaScriptInterface(), "JSInterface");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                ViseLog.d(url);
                ViseLog.d(userAgent);
                ViseLog.d(contentDisposition);
                ViseLog.d(mimetype);
                ViseLog.d(contentLength);
            }
        });
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
        super.onDestroy();
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
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
            webView.goBackPage();
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
