package x.com.hl_tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class hl_uri {
    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        //      也可用下面的方法拿到cursor  
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);  
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

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

}
