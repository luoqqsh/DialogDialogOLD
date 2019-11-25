package x.com.hl_tools;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class hl_gson {
    private Gson mGson;
    private static hl_gson gsonUtil;

    private hl_gson() {
    }

    public void release() {
        mGson = null;
        gsonUtil = null;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        return mGson;
    }

    public static hl_gson instance() {
        if (gsonUtil == null) {
            gsonUtil = new hl_gson();
        }
        return gsonUtil;
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        if (TextUtils.isEmpty(json) || classOfT == null) {
            return null;
        }
        return getGson().fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json) || type == null) {
            return null;
        }
        return getGson().fromJson(json, type);
    }

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return getGson().toJson(object);
    }


}
