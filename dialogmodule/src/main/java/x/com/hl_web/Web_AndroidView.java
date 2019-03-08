package x.com.hl_web;

import android.webkit.WebView;

public class Web_AndroidView {
    public WebView getmWebView() {
        return mWebView;
    }

    private WebView mWebView;

    public Web_AndroidView Web_AndroidView(Web_AndroidView WebView) {
        if (WebView == null) {
            WebView = new Web_AndroidView();
        }
        return WebView;
        
    }
}
