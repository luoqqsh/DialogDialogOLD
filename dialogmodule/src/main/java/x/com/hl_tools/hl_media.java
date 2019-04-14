package x.com.hl_tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import x.com.hl_log.ViseLog;

public class hl_media {
    /**
     * 刷新媒体库
     *
     * @param context context
     * @param path    文件路径
     */
    public static void updateMedia(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            context = context.getApplicationContext();
            try {
                MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }

    /**
     * 获取音乐的封面图
     *
     * @param album_id 封面图id
     * @param context  context
     * @return 返回后用Glide加载
     */
    private static String getAlbumArt(int album_id, Context context) {
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

    /**
     * 是否视频文件
     *
     * @param filePath 文件路径
     * @return 是否视频
     */
    public static boolean isVedioFile(String filePath) {
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
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }

    /**
     * 是否音频文件
     *
     * @param filePath 文件路径
     * @return 是否视频
     */
    public static boolean isAudioFile(String filePath) {
        return true;
    }
}
