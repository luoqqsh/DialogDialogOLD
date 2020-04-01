package x.com.dialogdialog.bbz;


import android.content.Context;


import java.util.HashMap;
import java.util.Map;
import x.com.hl_base.AppInit;
import x.com.hl_rx.jiu.HttpConfig;
import x.com.hl_rx.jiu.HttpMethodType;
import x.com.hl_rx.jiu.SimpleSubscriber;

public class HttpMethodConfig extends HttpConfig {

    private static HttpMethodConfig httpMethods;


    public static HttpMethodConfig instance() {
        if (httpMethods == null) {
            httpMethods = new HttpMethodConfig();
        }
        return httpMethods;
    }

    @Override
    public void reset() {
        httpMethods = new HttpMethodConfig();
    }

    @Override
    public boolean isDebug() {
        return AppInit.isDebug;
    }

    @Override
    public Context getContext() {
        return AppInit.getAppContext();
    }

    //    private Map<String, Object> sortAndAddSign(Map<String, Object> map) {
//        //动态壁纸的
//        if (map == null) return null;
//        map.put("sign", Tools.getSign(map));
//        return map;
//    }

    public void getTvList(SimpleSubscriber<String> subscriber) {
        Map<String, Object> paramsMap = new HashMap<>();
//        paramsMap = sortAndAddSign(paramsMap);
        doHttp(HttpMethodType.HttpGetUseUrl, "https://lenovodns.com/IPTV-List.json", paramsMap, subscriber, String.class);
    }
}