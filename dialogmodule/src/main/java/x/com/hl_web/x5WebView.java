package x.com.hl_web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;


public class x5WebView extends WebView {
    private Context mContext;

    public x5WebView(Context mContext) {
        super(mContext);
        this.mContext = mContext;
//        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
    }

    public x5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    public x5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
    }

    public x5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        this.mContext = context;
    }

    public x5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        this.mContext = context;
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
        Log.i("hl-x5web", "UserAgent:" + webSettings.getUserAgentString());
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        //String cacheDirPath = "路径";
        //webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
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
        Log.i("hl-x5web", "cookie:" + cookie);
    }

    public void loadUrl(String url) {
//        方式1：加载一个网页：
//        webView.loadUrl("http://www.google.com/");
//        方式2：加载apk包中的html页面
//        webView.loadUrl("file:///android_asset/test.html");
//        方式3：加载手机本地的html页面
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");
//        方式4：加载 HTML 页面的一小段内容.内容里不能出现 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，
//        若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
//        webView.loadData("需要截取展示的内容", "展示内容的类型", "字节码");
//        方式5：Android调用js代码。callJS()对应js相应的代码。该方法会刷新页面
//        JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
//        webView.loadUrl("javascript:callJS()");
//        方式6：Android调用js代码。要求安卓4.4以上，不会刷新页面。效率高
//        webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
        //此处为 js 返回的结果
//            }
//        });
//        方式7:协议拦截实现与js交互,根据以下内容进行判断
//        Uri uri = Uri.parse(url);
//        ViseLog.d(uri.getScheme() + uri.getAuthority());
        super.loadUrl(url);
    }

    public void destroyWebView() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        stopLoading();
        removeAllViewsInLayout();
        removeAllViews();
        setWebViewClient(null);
        CookieSyncManager.getInstance().stopSync();
        destroy();
    }
}
