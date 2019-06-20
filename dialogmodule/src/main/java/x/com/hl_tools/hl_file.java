package x.com.hl_tools;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static final String CAMERA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator;
    public static final String MUSIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + File.separator;
    public static final String DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;

    /**
     * 获取缓存路径
     *
     * @param context 上下文
     * @return 例子：/storage/emulated/0/Android/data/x.com.dialogdialog/cache
     */
    public static String getAppCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                return context.getExternalCacheDir().getPath();
            }
        }
        return context.getCacheDir().getPath();
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

    /**
     * 判断文件夹是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isFolderExist(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.e("hl_error", "path为空");
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isDirectory();
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
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        if (isFileExist(filePath)) {
            return deleteFile(new File(filePath));
        }
        Log.e("hl_error", "文件不存在");
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param filePath 文件夹路径
     * @return 删除结果
     */
    public static boolean deleteFolder(String filePath) {
        if (isFolderExist(filePath)) {
            return deleteFile(new File(filePath));
        }
        Log.e("hl_error", "文件夹不存在");
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

    /**
     * 把string写入文件
     *
     * @param context 上下文用于获取默认缓存目录
     * @param dstName 文件名
     * @param text    文本内容
     */
    private void writeStringToFile(Context context, String dstName, String text) {
        writeStringToFile(getAppCachePath(context), dstName, text);
    }

    /**
     * 把string写入文件
     *
     * @param dstPath 文件目录
     * @param dstName 文件名
     * @param text    文本内容
     */
    public static void writeStringToFile(String dstPath, String dstName, String text) {
        File file = hl_file.createFile(dstPath, dstName);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(text.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    public static File createFile(String parentPath, String dirName) {
        try {
            File dirFile = new File(parentPath);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("hl_error", "创建目录失败");
                    return null;
                }
            } else if (!dirFile.isDirectory()) {
                if (dirFile.delete()) {
                    return createFile(parentPath, dirName);
                } else {
                    Log.e("hl_error", "删除旧文件失败");
                    return null;
                }
            }
            File file = new File(parentPath, dirName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("hl_error", "新建文件失败");
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String readStringFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        return readStringFromFile(file);

    }

    public static String readStringFromFile(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String mimeTypeLine;
            while ((mimeTypeLine = br.readLine()) != null) {
                sb.append(mimeTypeLine);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

