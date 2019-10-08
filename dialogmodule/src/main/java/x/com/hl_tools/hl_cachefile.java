package x.com.hl_tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import x.com.hl_log.ViseLog;

public class hl_cachefile {

    private static hl_cachefile cachefile;
    private String parentPath;

    private hl_cachefile(Context context) {
        parentPath = hl_file.getAppCachePath(context);
        // /storage/emulated/0/Android/data/x.com.dialogdialog/cache
    }


    public static hl_cachefile instance(Context context) {
        if (cachefile == null) {
            cachefile = new hl_cachefile(context);
        }
        return cachefile;
    }


    public <T> T readObject(String key) {
        return readObject(key, null);
    }

    public <T> T readObject(String key, T defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        File file = new File(parentPath, hl_encrypt.Md5(key));
        if (file.isFile() && file.exists() && file.length() > 0) {
            String string = hl_file.readStringFromFile(file);
            try {
                if (TextUtils.isEmpty(string)) {
                    return defaultValue;
                }
                byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
                ByteArrayInputStream is = new ByteArrayInputStream(base64);
                T object1;
                ObjectInputStream bis = new ObjectInputStream(is);
                Object object = bis.readObject();
                object1 = (T) object;
                return object1;
            } catch (Exception e) {
                e.printStackTrace();
                remove(key);
                ViseLog.e("hl_error", "读取异常");
            }
        }
        return defaultValue;
    }

    public void saveObject(String key, Object object) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (object == null) {
            remove(key);
            return;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(object);
            String text = new String(Base64.encode(os.toByteArray(), Base64.DEFAULT));
            hl_file.writeStringToFile(parentPath, hl_encrypt.Md5(key), text);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e("hl_error", "写入异常");
        }
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            File file = new File(parentPath, hl_encrypt.Md5(key));
            if (file.exists()) {
                if (!file.delete()) {
                    ViseLog.e("hl_error", "删除旧缓存失败");
                }
            }
        }
    }

    public void clear() {
        hl_file.deleteFolder(parentPath);
    }
}
