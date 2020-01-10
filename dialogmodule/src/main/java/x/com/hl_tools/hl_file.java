package x.com.hl_tools;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.DialogTools.EncodingDetect;

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

    //以下方法在安卓Q之后的版本能获取得到，但无法使用
    public static final String SDROOT = Environment.getExternalStorageDirectory().getPath();
    public static final String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    public static final String MUSIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    public static final String MOVIE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    public static final String ALARMS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    public static final String RINGTONES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    public static final String DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static final String CAMERA = DCIM + File.separator + "Camera";
    public static final String DIALOG = SDROOT + File.separator + "dialog";
    public static final String QQFILE = SDROOT + File.separator + "Tencent" + File.separator + "QQfile_recv";

    public static final int TYPE_DOC = 0;
    public static final int TYPE_APK = 1;
    public static final int TYPE_ZIP = 2;

    /**
     * 安卓Q获取沙盒目录
     * /storage/emulated/0/Android/data/包名/cache
     *
     * @param context 上下文
     * @return 文件夹
     */
    public static File getQdir(Context context) {
        return context.getExternalCacheDir();
    }

    public static File getQdir(Context context, String environment) {
        return context.getExternalFilesDir(environment);
    }

    /**
     * 获取APP缓存路径
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
    ////////////////存在判断////////////


    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            ViseLog.e("hl_error", path + " 文件路径为空");
            return false;
        }
        File file = new File(path);
        if (file.length() == 0) {
            ViseLog.e("hl_error", path + " 文件存在，但大小为0");
        }
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
            ViseLog.showLog("hl_error", "path为空");
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    /**
     * 判断文件夹是否是空文件夹
     *
     * @param path 文件路径
     * @return 是否是空文件夹
     */
    public static boolean isFolderNull(String path) {
        if (isFolderExist(path)) {
            File file = new File(path);
            String[] list = file.list();
            return list == null || list.length == 0;
        }
        return false;
    }

    ////////////////新建/////////////////
    //创建文件夹-文件路径
    public static boolean createFolder(String filePath) {
        File dirFile = new File(filePath);
        return createFolder(dirFile);
    }

    //创建文件夹-文件夹路径+文件夹名
    public static boolean createFolder(String parentPath, String dirName) {
        File dirFile = new File(parentPath, dirName);
        return createFolder(dirFile);
    }

    //创建文件夹-文件路径
    public static boolean createFileFolder(String filePath) {
        File dirFile = new File(filePath);
        return createFileFolder(dirFile);
    }

    //创建文件夹-文件路径+文件名
    public static boolean createFileFolder(String parentPath, String dirName) {
        File dirFile = new File(parentPath, dirName);
        return createFileFolder(dirFile);
    }

    public static boolean createFolder(File file) {
        if (!(file.exists() && file.isDirectory())) {
            return (file.mkdirs());
        } else {
            ViseLog.showLog("hl_error", file.getAbsolutePath() + "该文件夹已存在");
        }
        return false;
    }

    public static boolean createFileFolder(File file) {
        if (!(file.exists() && file.isDirectory())) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                return parentFile.mkdirs();
            } else {
                return false;
            }
        } else {
            ViseLog.showLog("hl_error", file.getAbsolutePath() + "该文件夹已存在");
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param parentPath 文件目录
     * @param dirName    文件名（含后缀）
     * @param isReplace  是否替换
     * @return 创建结果
     */
    public static File createFile(String parentPath, String dirName, boolean isReplace) {
        try {
            File dirFile = new File(parentPath);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    ViseLog.showLog("hl_error", "创建目录失败");
                    return null;
                }
            }
            if (!dirFile.isDirectory()) {
                if (dirFile.delete()) {
                    return createFile(parentPath, dirName, isReplace);
                } else {
                    ViseLog.showLog("hl_error", "删除旧文件失败");
                    return null;
                }
            }
            File file = new File(parentPath, dirName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    ViseLog.showLog("hl_error", "新建文件失败");
                    return null;
                }
            } else {
                if (isReplace) {
                    if (file.delete()) {
                        if (file.createNewFile()) {
                            return file;
                        } else {
                            ViseLog.showLog("hl_error", "删除后新建文件失败");
                            return null;
                        }
                    } else {
                        ViseLog.showLog("hl_error", "文件已存在且无法删除");
                    }
                } else {
                    ViseLog.showLog("hl_info", "文件已存在");
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            ViseLog.showLog("hl_error", "新建文件失败" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    ////////////////删除/////////////////

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
        ViseLog.showLog("hl_error", filePath + "文件不存在");
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
        ViseLog.showLog("hl_error", filePath + "文件夹不存在");
        return false;
    }

    /**
     * 清空文件夹
     *
     * @param filePath 文件夹路径
     * @return 删除结果
     */
    public static boolean clearFolder(String filePath) {
        if (isFolderExist(filePath)) {
            return deleteFile(new File(filePath), true);
        }
        ViseLog.showLog("hl_error", filePath + "文件夹不存在");
        return false;
    }

    public static boolean deleteFile(File file) {
        return deleteFile(file, false);
    }

    public static boolean deleteFile(File file, boolean isClear) {
        if (file == null || !file.exists()) {
            ViseLog.showLog("hl_error", "文件不存在");
            return false;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (!deleteFile(f)) {
                        ViseLog.showLog("hl_error", f.getName() + "删除失败");
                    }
                }
            }
        }
        if (isClear) {
            return true;
        } else {
            return file.delete();
        }
    }

    ////////////////复制移动/////////////////

    /**
     * 文件复制
     *
     * @param inputFilePath  源文件
     * @param outputFilePath 输出文件
     * @param isReplace      若存在是否替换
     * @return 结果
     */
    public static boolean copyFile(String inputFilePath, String outputFilePath, boolean isReplace) {
        try {
            if (!isFileExist(inputFilePath)) {
                ViseLog.showLog("hl_error", inputFilePath + "源文件不存在");
                return false;
            }
            File file = new File(outputFilePath);
            if (!file.exists()) {
                createFile(file.getParent(), file.getName(), isReplace);
            } else {
                if (isReplace) {
                    if (file.delete()) {
                        createFile(file.getParent(), file.getName(), true);
                    } else {
                        ViseLog.showLog("hl_error", "文件删除失败");
                        return false;
                    }
                } else {
                    ViseLog.showLog("hl_error", "目标文件已存在，请使用强制复制");
                    return false;
                }
            }
            return copyFile(new FileInputStream(inputFilePath), new FileOutputStream(outputFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean moveFile(String inputFilePath, String outputFilePath, boolean isReplace) {
        if (copyFile(inputFilePath, outputFilePath, isReplace)) {
            File file = new File(inputFilePath);
            return deleteFile(file);
        }
        return false;
    }

    public static boolean copyFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean moveFolder(String inputFilePath, String outputFilePath) {
        if (copyFolder(inputFilePath, outputFilePath)) {
            return deleteFolder(inputFilePath);
        }
        return false;
    }

    public static boolean copyFolder(String inputFilePath, String outputFilePath) {
        File in = new File(inputFilePath);
        createFolder(outputFilePath, in.getName());
        File file = new File(outputFilePath, in.getName());
        return copyFolder(inputFilePath, file.getAbsolutePath(), true);
    }

    private static boolean copyFolder(String inputFilePath, String outputFilePath, boolean isReplace) {
        try {
            if (!isFolderExist(inputFilePath)) {
                ViseLog.showLog("hl_error", inputFilePath + "源文件夹不存在");
                return false;
            }
            File in = new File(inputFilePath);
            File[] listFiles = in.listFiles();
            ViseLog.d(listFiles);
            File out = new File(outputFilePath);
            if (!out.exists()) {
                out.mkdirs();
            }
            if (listFiles != null) {
                for (File f : listFiles) {
                    if (f.isFile()) {
                        //文件
                        copyFile(f.getPath(), outputFilePath + File.separator + f.getName(), isReplace); //调用文件拷贝的方法
                    } else if (f.isDirectory()) {
                        //文件夹
                        copyFolder(f.getPath(), outputFilePath + File.separator + f.getName(), isReplace);//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //////////////////////string读取写入//////////////////////////////

    /**
     * 把string写入文件
     *
     * @param context 上下文用于获取默认缓存目录
     * @param dstName 文件名
     * @param text    文本内容
     */
    public void writeStringToFile(Context context, String dstName, String text) {
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
        File file = hl_file.createFile(dstPath, dstName, true);
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

    /**
     * 从文件读取string
     *
     * @param filePath 文件路径
     * @return string
     */
    public static String readStringFromFile(String filePath) {
        return readStringFromFile(filePath, "UTF-8");
    }

    public static String readStringFromFile(String filePath, String code) {
        if (TextUtils.isEmpty(filePath)) {
            ViseLog.showLog("hl_error", "路径不可为空");
            return null;
        }
        File file = new File(filePath);
        return readStringFromFile(file, code);
    }

    /**
     * 从文件读取string
     *
     * @param file 文件
     * @return string
     */
    public static String readStringFromFile(File file) {
        return readStringFromFile(file, "UTF-8");
    }

    public static String readStringFromFile(File file, String code) {
        if (!file.exists()) {
            ViseLog.showLog("hl_error", file.getAbsolutePath() + "读取的文件不存在");
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
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

    /**
     * 重命名文件
     *
     * @param path    地址
     * @param newName 文件名
     * @return 结果
     */
    public static boolean renameFile(String path, String newName) {
        if (!isFileExist(path)) {
            ViseLog.showLog("hl_error", "文件不存在");
            return false;
        }
        File newFile = new File(path);
        return renameFile(newFile, newName);
    }

    public static boolean renameFile(File file, String newName) {
        if (!file.exists()) {
            ViseLog.showLog("hl_error", "文件不存在");
            return false;
        }
        File newFile = new File(file.getParent(), newName);
        return file.renameTo(newFile);
    }

    /**
     * 获取文件MD5值
     *
     * @param path 路径
     * @return md5
     */
    public static String getFileMD5(String path) {
        return getFileMD5(new File(path));
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return "ERROR,文件不存在";
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 递归取得文件夹大小
     *
     * @param file 文件
     * @return long 大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    if (value.isDirectory()) {
                        size = size + getFileSize(value);
                    } else {
                        size = size + value.length();
                    }
                }
            }
        }
        return size;
    }

    /**
     * 获取文件编码类型
     *
     * @param path 路径
     * @return 编码类型，UTF-8，GBK，ANSI。。。
     */
    public static String getEncode(String path) {
        return EncodingDetect.getJavaEncode(path);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx")) {
            return TYPE_DOC;
        } else if (path.endsWith(".apk")) {
            return TYPE_APK;
        } else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            return TYPE_ZIP;
        } else {
            return -1;
        }
    }

    /**
     * 通过文件名获取文件图标
     */
    public static int getFileIconByPath(String path) {
        path = path.toLowerCase();
        int iconId = R.mipmap.icon_error;
        if (path.endsWith(".txt")) {
            iconId = R.mipmap.icon_error;
        } else if (path.endsWith(".doc") || path.endsWith(".docx")) {
            iconId = R.mipmap.icon_error;
        } else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
            iconId = R.mipmap.icon_error;
        } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
            iconId = R.mipmap.icon_error;
        } else if (path.endsWith(".xml")) {
            iconId = R.mipmap.icon_error;
        } else if (path.endsWith(".htm") || path.endsWith(".html")) {
            iconId = R.mipmap.icon_error;
        }
        return iconId;
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg") || path.toLowerCase().endsWith(".png")) {
            return true;
        }
        return false;
    }


    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 从文件的全名得到文件的拓展名
     *
     * @param filename
     * @return
     */
    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }

    /**
     * 读取文件的修改时间
     *
     * @param f
     * @return
     */
    public static String getModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        // System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }
}