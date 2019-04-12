package x.com.hl_tools;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;

public class hl_file {
//  data目录        Environment.getDataDirectory() = /data
//  下载缓存         Environment.getDownloadCacheDirectory()= /cache
//  外置SD卡目录     Environment.getExternalStorageDirectory() = /mnt/sdcard
//    Environment.getExternalStoragePublicDirectory(“test”)= /mnt/sdcard/test
//    Environment.getRootDirectory() = /system
//    getPackageCodePath() = /data/app/com.my.app-1.apk
//    getPackageResourcePath() = /data/app/com.my.app-1.apk
//    getCacheDir() = /data/data/com.my.app/cache
//    getDatabasePath(“test”)= /data/data/com.my.app/databases/test
//    getDir(“test”, Context.MODE_PRIVATE)= /data/data/com.my.app/app_test
//    getExternalCacheDir()	= /mnt/sdcard/Android/data/com.my.app/cache
//    getExternalFilesDir(“test”)= /mnt/sdcard/Android/data/com.my.app/files/test
//    getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
//    getFilesDir()	= /data/data/com.my.app/files

    //内置存储根目录/storage/emulated/0
    public static final String SDROOT = Environment.getExternalStorageDirectory().getPath();

    /**
     * 获取缓存路径
     *
     * @param context 上下文
     * @return 例子：/storage/emulated/0/Android/data/x.com.dialogdialog/cache
     */
    public static String getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }
}

