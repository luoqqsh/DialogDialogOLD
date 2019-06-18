package x.com.hl_tools;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
    //安卓Q沙盒目录Context.getExternalFilesDir() =
    //安卓Q沙盒图片Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) =
    public static final String SDROOT = Environment.getExternalStorageDirectory().getPath();
    public static final String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator;
    public static final String DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;

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
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.e("hl_error", "path为空");
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (isFileExist(filePath)) {
            return deleteFile(new File(filePath));
        }
        Log.e("hl_error", "文件不存在");
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            Log.e("hl_error", "文件不存在");
            return false;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (!deleteFile(f)) {
                        Log.e("hl_error", f.getName() + "删除失败");
                    }
                }
            }
        }
        return file.delete();
    }

    public static boolean copyFile(String sourceFilePath, String resultFilePath) {
        try {
            return writeFile(new File(resultFilePath), new FileInputStream(sourceFilePath), false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            if (!file.exists()) {
                createFile(file.getParent(), file.getName());
            }
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (o != null) {
                    o.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static File createFile(String dirPath, String fileName) {
        try {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("hl_error", "创建目录失败");
                    return null;
                }
            } else if (!dirFile.isDirectory()) {
                boolean delete = dirFile.delete();
                if (delete) {
                    return createFile(dirPath, fileName);
                } else {
                    Log.e("hl_error", "删除旧文件失败");
                    return null;
                }
            }
            File file = new File(dirPath, fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("hl_error","新建文件失败");
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

