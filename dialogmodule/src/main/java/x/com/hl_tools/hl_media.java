package x.com.hl_tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import x.com.hl_log.ViseLog;

public class hl_media {
    /**
     * 刷新媒体库
     *
     * @param context context
     * @param path    文件路径
     */
    public static void updateMedia(Context context, String path) {
        try {
            if (hl_file.isFileExist(path)) {
                MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            } else {
                ViseLog.e("hl_error", "文件路径错误：" + path);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void updateMedias(Context context, String path) {
        try {
            if (hl_file.isFolderExist(path)) {
                List<String> file = new ArrayList<>();
                File[] fileList = new File(path).listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        if (hl_file.isFileExist(f.getAbsolutePath())) {
                            file.add(f.getAbsolutePath());
                        }
                    }
                }
                MediaScannerConnection.scanFile(context.getApplicationContext(), file.toArray(new String[0]), null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            } else {
                ViseLog.e("hl_error", "文件夹路径错误：" + path);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    /**
     * 获取音乐的封面图
     *
     * @param album_id 封面图id
     * @param context  context
     * @return 返回后用Glide加载
     */
    public static String getAlbumArt(int album_id, Context context) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id), projection, null, null, null);
        if (cur == null) return null;
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        return album_art;
    }

    //安卓q方法
    public static Bitmap getThumbnail(ContentResolver cr, long videoId) {
        return MediaStore.Video.Thumbnails.getThumbnail(cr, videoId, MediaStore.Video.Thumbnails.MINI_KIND, null);
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbnail(int id, Context context) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        return bitmap;
    }

    /**
     * 是否视频文件
     *
     * @param filePath 文件路径
     * @return 是否视频
     */
    public static boolean isVideoFile(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileName);
        return file.exists() && type.contains("video");
    }

    /**
     * 是否图片文件
     *
     * @param filePath 文件路径
     * @return 是否图片
     */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outWidth != -1;
    }

    /**
     * 获取00：00格式时间
     *
     * @param timeMs 时长，单位毫秒
     * @return 00：00格式文本
     */
    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * intent跳转到图库页面，需要处理onActivityResult回调
     *
     * @param context 上下文
     */
    public static void intentToGallery(Activity context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, requestCode);
    }

    public static String getUriStringById(int id) {
        return MediaStore.Video.Media
                .EXTERNAL_CONTENT_URI
                .buildUpon()
                .appendPath(String.valueOf(id)).build().toString();
    }

    //这种方法最大的问题即是，对应于一个同步 I/O 调用，易造成线程等待。因此，目前对于 MediaStore 中扫描出来的文件可能不存在的情况，没有直接的好方法可以解决过滤。
    public static boolean isContentUriExists(Context context, Uri uri) {
        if (null == context || uri == null) {
            return false;
        }
        ContentResolver cr = context.getContentResolver();
        try {
            AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
            if (null == afd) {
                return false;
            } else {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
}
