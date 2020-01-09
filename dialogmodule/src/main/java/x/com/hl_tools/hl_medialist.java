package x.com.hl_tools;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import x.com.hl_model.ImgFolderBean;
import x.com.hl_model.MediaFileBean;
import x.com.hl_tools.model.AppInfo;

public class hl_medialist {
    /**
     * 安卓Q获取图片列表
     */
    public static List<MediaFileBean> getImgList(Context context) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        List<MediaFileBean> list = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Thumbnails.DATA
        };
        //全部图片
        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        //指定格式
        String[] whereArgs = {"image/jpeg", "image/png", "image/jpg"};
        //查询
        Cursor mCursor = context.getContentResolver().query(mImageUri, projection, null, null, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                int thumbPathIndex = mCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                int timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                int pathIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int id = mCursor.getColumnIndex(MediaStore.Images.Media._ID);

                long date = mCursor.getLong(timeIndex) * 1000;
                String fileUri, thumbPath;
                //适配Android Q
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    fileUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(mCursor.getInt(id))).build().toString();
                    if (hl_media.isContentUriExists(context, Uri.parse(fileUri))) {
                        MediaFileBean mediaFileBean = new MediaFileBean();
                        mediaFileBean.setMediaId(id);
                        mediaFileBean.setMediaPath(mCursor.getString(pathIndex));
                        mediaFileBean.setDate(date);
                        mediaFileBean.setMediaPathUri(fileUri);
                        list.add(mediaFileBean);
                    }
                } else {
                    thumbPath = mCursor.getString(thumbPathIndex);
                    fileUri = mCursor.getString(pathIndex);
                    //判断文件是否存在，存在才去加入
                    if (hl_file.isFileExist(fileUri)) {
                        MediaFileBean mediaFileBean = new MediaFileBean();
                        mediaFileBean.setMediaId(id);
                        mediaFileBean.setMediaPath(fileUri);
                        mediaFileBean.setThumbPath(thumbPath);
                        mediaFileBean.setDate(date);
                        mediaFileBean.setMediaPathUri(hl_uri.getPhotoUri(mCursor).toString());
                        list.add(mediaFileBean);
                    }
                }
            }
            mCursor.close();
            return list;
        }
        return null;
    }

    /**
     * 安卓Q获取视频列表
     *
     * @param context
     * @return
     */
    public static List<MediaFileBean> getVideoList(Context context) {
        Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        List<MediaFileBean> videoList = new ArrayList<>();
        String[] projection = {MediaStore.Video.Thumbnails._ID
                , MediaStore.Video.Thumbnails.DATA
                , MediaStore.Video.Media.DURATION
                , MediaStore.Video.Media.SIZE
                , MediaStore.Video.Media.DATE_ADDED
                , MediaStore.Video.Media.RESOLUTION
                , MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.DATE_MODIFIED};
        //全部视频
        String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=?";
        String[] whereArgs = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg"};
        Cursor mCursor = context.getContentResolver().query(mVideoUri, projection, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC ");
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                int thumbPathIndex = mCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                int timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                int pathIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String resolution = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));//分辨率
                long size = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)) / 1024; //单位kb
                long date = mCursor.getLong(timeIndex) * 1000;
                int videoId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));
                String path;
                //适配Android Q
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    path = MediaStore.Video.Media
                            .EXTERNAL_CONTENT_URI
                            .buildUpon()
                            .appendPath(String.valueOf(videoId)).build().toString();
                } else {
                    path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                }
                long duration = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                if (size < 0) {
                    //某些设备获取size<0，直接计算
                    size = new File(path).length() / 1024;
                }
                String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                //用于展示相册初始化界面
                //需要判断当前文件是否存在  一定要加，不然有些文件已经不存在图片显示不出来。这里适配Android Q
                synchronized (context) {
                    boolean fileIsExists;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        fileIsExists = hl_media.isContentUriExists(context, Uri.parse(path));
                        if (fileIsExists) {
                            MediaFileBean mediaFileBean = new MediaFileBean(videoId, path, resolution, size, date, duration);
                            mediaFileBean.setMediaPathUri(hl_uri.getVideoUri(mCursor).toString());
                            videoList.add(mediaFileBean);
                        }
                    } else {
                        fileIsExists = hl_file.isFileExist(path);
                        if (fileIsExists) {
                            MediaFileBean mediaFileBean = new MediaFileBean(videoId, path, resolution, size, date, duration);
                            mediaFileBean.setMediaPathUri(hl_uri.getVideoUri(mCursor).toString());
                            videoList.add(mediaFileBean);
                        }
                    }
                }
            }
            mCursor.close();
            return videoList;
        }
        return null;
    }

    /**
     * 获取本机音乐列表
     *
     * @param context 上下文
     * @return 列表
     */
    public static List<MediaFileBean> getMusics(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        ArrayList<MediaFileBean> musics = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (c != null) {
                while (c.moveToNext()) {
                    MediaFileBean music = new MediaFileBean();
                    music.setFileName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    music.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    music.setAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    music.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    music.setSize(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    music.setDuration(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    music.setMediaId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    music.setAlbumId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                    music.setMediaPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
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
    public static List<MediaFileBean> getVideos(Context context) {
        ContentResolver mContentResolver = context.getContentResolver();
        List<MediaFileBean> videos = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            if (c != null) {
                while (c.moveToNext()) {
                    MediaFileBean video = new MediaFileBean();
                    //注意，DATA 数据在 Android Q 以前代表了文件的路径，但在 Android Q上该路径无法被访问，因此没有意义。
                    video.setMediaPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                    video.setMediaId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));// 视频的id
                    video.setFileName(c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))); // 视频名称
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

    /**
     * 通过文件类型得到相应文件的集合
     **/
    public List<MediaFileBean> getFilesByType(int fileType, Context context) {
        List<MediaFileBean> files = new ArrayList<>();
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

                    if (hl_file.getFileType(path) == fileType) {
                        if (!hl_file.isFileExist(path)) {
                            continue;
                        }
                        long size = c.getLong(sizeindex);
                        MediaFileBean fileBean = new MediaFileBean(path, hl_file.getFileIconByPath(path));
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
                if (hl_file.isPicFile(path)) {
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
