package x.com.hl_tools;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_tools.DialogTest.phone.FileUtils;
import x.com.hl_tools.DialogTest.phone.bean.FileBean;
import x.com.hl_tools.DialogTest.phone.bean.ImgFolderBean;
import x.com.hl_tools.DialogTest.phone.bean.Music;
import x.com.hl_tools.DialogTest.phone.bean.Video;
import x.com.hl_tools.model.AppInfo;

public class hl_medialist {
    /**
     * 获取本机音乐列表
     *
     * @param context 上下文
     * @return 列表
     */
    public static List<Music> getMusics(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        ArrayList<Music> musics = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径
                    if (!new File(path).exists()) {
                        continue;
                    }
                    Music music = new Music();
                    music.setFileName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    music.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    music.setAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    music.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    music.setSize(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    music.setDuration(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    music.setMusicId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    music.setAlbumId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                    music.setPath(path);
                    music.setType(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)));
                    music.setYear(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)));
                    musics.add(music);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return musics;
    }

    /**
     * 获取本机视频列表
     *
     * @return
     */
    public static List<Video> getVideos(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        List<Video> videos = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                    if (!new File(path).exists()) {
                        continue;
                    }
                    Video video = new Video();
                    video.setPath(path);
                    video.setId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));// 视频的id
                    video.setName(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))); // 视频名称
                    video.setResolution(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))); //分辨率
                    video.setSize(c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));// 大小
                    video.setDuration(c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));// 时长
                    video.setDate(c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)));//修改时间
                    videos.add(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videos;
    }

    //
    // 获取视频缩略图
    public Bitmap getVideoThumbnail(int id, Context context) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        return bitmap;
    }
//

    /**
     * 通过文件类型得到相应文件的集合
     **/
    public List<FileBean> getFilesByType(int fileType, Context context) {
        List<FileBean> files = new ArrayList<>();
        // 扫描files文件库
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_size"}, null, null, null);
            int dataindex = 0;
            int sizeindex = 0;
            if (c != null) {
                dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            }
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(dataindex);

                    if (FileUtils.getFileType(path) == fileType) {
                        if (!FileUtils.isExists(path)) {
                            continue;
                        }
                        long size = c.getLong(sizeindex);
                        FileBean fileBean = new FileBean(path, FileUtils.getFileIconByPath(path));
                        files.add(fileBean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }


    /**
     * 得到图片文件夹集合
     */
    public static List<ImgFolderBean> getImageFolders(Context context) {
        List<ImgFolderBean> folders = new ArrayList<>();
        // 扫描图片
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            List<String> mDirs = new ArrayList<>();//用于保存已经添加过的文件夹目录
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;

                    String dir = parentFile.getAbsolutePath();
                    if (mDirs.contains(dir))//如果已经添加过
                        continue;

                    mDirs.add(dir);//添加到保存目录的集合中
                    ImgFolderBean folderBean = new ImgFolderBean();
                    folderBean.setDir(dir);
                    folderBean.setFistImgPath(path);
                    if (parentFile.list() == null)
                        continue;
                    int count = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(count);
                    folders.add(folderBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return folders;
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (!directory.exists()) {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String path = file.getAbsolutePath();
                if (FileUtils.isPicFile(path)) {
                    imgPaths.add(path);
                }
            }
        }
        return imgPaths;
    }

    /**
     * 获取已安装apk的列表
     */
    public static List<AppInfo> getAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        //获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获得所有的安装包
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        //遍历每个安装包，获取对应的信息
        for (PackageInfo packageInfo : installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.setSourceDir(packageInfo.applicationInfo.sourceDir);
            appInfo.setVersionCode(packageInfo.versionCode);
            appInfo.setVersionName(packageInfo.versionName);
            appInfo.setFlags(packageInfo.applicationInfo.flags);
            appInfo.setName(packageInfo.applicationInfo.name);
            appInfo.setFirstInstallTime(packageInfo.firstInstallTime);
            appInfo.setLastUpdateTime(packageInfo.lastUpdateTime);
            appInfo.setTargetSdkVersion(packageInfo.applicationInfo.targetSdkVersion);
            appInfo.setDataDir(packageInfo.applicationInfo.dataDir);
            appInfo.setEnabled(packageInfo.applicationInfo.enabled);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appInfo.setMinSdkVersion(packageInfo.applicationInfo.minSdkVersion);
            }
            appInfo.setProcessName(packageInfo.applicationInfo.processName);
            appInfo.setUid(packageInfo.applicationInfo.uid);
            appInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            appInfo.setApkName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            appInfo.setApkPackageName(packageInfo.packageName);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
