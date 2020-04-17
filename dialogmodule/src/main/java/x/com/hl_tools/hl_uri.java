package x.com.hl_tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import x.com.hl_dialog.R;

public class hl_uri {
    /**
     * 在读取和写入时，我们可以借助 Content Uri 从 ContentResolver 里面拿到 AssetFileDescriptor，然后就可以拿到 InputSteam 或 OutputStream
     */
    public static void copy(File src, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        FileInputStream istream = new FileInputStream(src);
        FileOutputStream ostream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
        try {
            hl_stream.streamCopy(istream, ostream);
        } finally {
            ostream.close();
            istream.close();
        }
    }

    public static void copy(ParcelFileDescriptor parcelFileDescriptor, File dst) throws IOException {
        FileInputStream istream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
        FileOutputStream ostream = new FileOutputStream(dst);
        try {
            hl_stream.streamCopy(istream, ostream);
        } finally {
            ostream.close();
            istream.close();
        }
    }

    public static void copy(InputStream ist, OutputStream ost) throws IOException {
        byte[] buffer = new byte[4096];
        int byteCount = 0;
        while ((byteCount = ist.read(buffer)) != -1) {  // 循环从输入流读取 buffer字节
            ost.write(buffer, 0, byteCount);        // 将读取的输入流写入到输出流
        }
    }

    //保存媒体文件到公共区域，这里仅以 Video 示例，Image、Downloads 基本类似
    public static Uri insertVideoIntoMediaStore(Context context, String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static Uri getPhotoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getVideoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getMediaUri(Cursor cursor, Uri uri) {
        String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        return Uri.withAppendedPath(uri, id);
    }

    /**
     * string类型的Uri转Uri类型
     *
     * @param uri string格式的uri
     * @return Uri
     */
    public static Uri str2Uri(String uri) {
        return Uri.parse(uri);
    }

    public static Uri bitmap2Uri(Context context, Bitmap bitmap) {
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
    }

    public static Bitmap uri2Bitmap(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //*获取Res资源的url  ContentResolver.SCHEME_ANDROID_RESOURCE*/
    public static Uri getResourceUri(Context context, int resource) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resource);
    }

    public static Uri getResUri(Context context, int resource) {
        return Uri.parse("res:///" + R.mipmap.ic_launcher);
    }

    //*获取asset资源的url,ContentResolver.SCHEME_FILE*/
    public static Uri getAssessUri(int fileName) {
        return Uri.parse("file:///android_asset/" + fileName);
    }

    /**
     * 要将Uri提供给外部使用时，Android7.0必须使用FileProvider来转换，注意FLAG_GRANT_READ_URI_PERMISSION
     * 比如使用系统裁剪，比如安装APP
     * 注：拍照不要用这个，不然会回调失败
     */
    public static Uri getUri(Context context, Intent intent, File file) {
        return getUri(context, intent, file, false);
    }

    public static Uri getUri(Context context, Intent intent, File file, boolean isUseForCamera) {
        if (isUseForCamera) {
//            targetSdkVersion<=23 or 手机Android版本< 7.0
            if (context.getApplicationInfo().targetSdkVersion <= 23 || Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return Uri.fromFile(file);
            }
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (intent != null) {
                if (!isUseForCamera) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".dialog.fileProvider", file);
        } else {
            if (intent != null) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}