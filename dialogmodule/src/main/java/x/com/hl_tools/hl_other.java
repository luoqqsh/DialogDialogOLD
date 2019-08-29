package x.com.hl_tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class hl_other {
    //key为渠道名的key，对应友盟的 UMENG_CHANNEL
    public static String getChannel(Context context, String key) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    //判断是否是json数据
    public static boolean isJson(String str) {
        try {
            Gson g = new Gson();
            g.fromJson(str, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson2(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static void joinQQ(Context context, String qqCode) {
        String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqCode + "&version=1";
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
    }

    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
