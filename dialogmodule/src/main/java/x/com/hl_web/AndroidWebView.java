package x.com.hl_web;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vise.log.ViseLog;

import java.util.List;

public class AndroidWebView extends WebView {
    private Context mContext;

    public AndroidWebView(Context mContext) {
        super(mContext);
        this.mContext = mContext;
//        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
    }

    public AndroidWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AndroidWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AndroidWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebSetting() {
        WebSettings webSettings = getSettings();
        //缩放操作
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小 
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //缓存模式如下：
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.setAllowFileAccess(true);//使用 file 域加载的 js代码能够使用进行同源策略跨域访问,使其不能加载本地的 html 文件
        //有需要使用file协议的话，加上
//        webSettings.setAllowFileAccess(true);
//        if (url.startsWith("file://")) {
//            webSettings.setJavaScriptEnabled(false);
//        } else {
//            webSettings.setJavaScriptEnabled(true);
//        }
        webSettings.setAllowFileAccessFromFileURLs(false);//设置是否允许通过 file url 加载的 Js代码读取其他的本地文件
        webSettings.setAllowUniversalAccessFromFileURLs(false);//设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        //其他细节操作
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setSavePassword(true);//默认开启密码保存功能，密码会被明文保到 /data/data/com.package.name/databases/webview.db 中
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染优先级
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        webSettings.setGeolocationEnabled(true);//定位功能
        webSettings.setSaveFormData(true);//是否保存表单
        webSettings.setSavePassword(true);//是否保存密码
        webSettings.setSupportMultipleWindows(true);//代表支持多窗口打开
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗,支持通过JS打开新窗口 
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 把图片加载放在最后来加载渲染
        webSettings.setBlockNetworkImage(false);
        // 特征标记
        webSettings.setUserAgentString(webSettings.getUserAgentString() + "3500game");

        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        //String cacheDirPath = "路径";
        //webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
    }

    public void initWebClient() {
        //处理各种通知 & 请求事件
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                //方式1：加载一个网页：
//                webView.loadUrl("http://www.google.com/");
                //方式2：加载apk包中的html页面
//                webView.loadUrl("file:///android_asset/test.html");
                //方式3：加载手机本地的html页面
//                webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");
                //方式4：加载 HTML 页面的一小段内容.内容里不能出现 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，
                //若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
//                webView.loadData("需要截取展示的内容", "展示内容的类型", "字节码");
                //方式5：Android调用js代码。callJS()对应js相应的代码。该方法会刷新页面
                //JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
//                webView.loadUrl("javascript:callJS()");
                //方式6：Android调用js代码。要求安卓4.4以上，不会刷新页面。效率高
//                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        //此处为 js 返回的结果
//                    }
//                });
                //方式7:协议拦截实现与js交互,根据以下内容进行判断
//                Uri uri = Uri.parse(url);
//                ViseLog.d(uri.getScheme() + uri.getAuthority());
                if (!url.startsWith("http")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    if (isIntentExisting(mContext, intent)) {
                        mContext.startActivity(intent);
                    }
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                super.onPageStarted(webView, url, bitmap);
                ViseLog.d(url);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                ViseLog.d(url);
            }

            //每一个资源（比如图片）的加载都会调用一次。
            @Override
            public void onLoadResource(WebView webView, String url) {
                super.onLoadResource(webView, url);
                ViseLog.d(url);
            }

            //请求错误处理。
            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                super.onReceivedError(webView, errorCode, description, failingUrl);
                ViseLog.d(failingUrl);
                ViseLog.d(errorCode);
                ViseLog.d(description);
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });
    }

    public static boolean isIntentExisting(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return resolveInfo.size() > 0;
    }

    public void initWebChromeClient() {
        //辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
        setWebChromeClient(new WebChromeClient() {
            //获得网页加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ViseLog.d(newProgress);
            }

            //获得网页中的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                ViseLog.d(title);
            }

            //支持javascript的警告框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                ViseLog.d(message);
                ViseLog.d(result);
                return true;
            }

            //支持javascript的确认框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                ViseLog.d(message);
                ViseLog.d(result);
                // 返回布尔值：判断点击时确认还是取消。true表示点击了确认；false表示点击了取消；
                return true;
            }

            //支持javascript输入框
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                return true;
            }
        });
    }
    public void initDownload(){
        
    }

    public void setCookies(String url, String[] cookies) {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        for (int i = 0; i < cookies.length; i++) {
            cookieManager.setCookie(url, cookies[i]);
        }
        CookieSyncManager.getInstance().sync();
        String cookie = cookieManager.getCookie(url);
    }

    public void goBackPage() {
        if (canGoBack()) {
            goBack();
        }
    }

    public void goForwardPage() {
        if (canGoForward()) {
            goForward();
        }
    }
}
