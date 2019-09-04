package x.com.hl_tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class hl_img {
    // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public static String imgToStr64(InputStream in) throws IOException {
        if (in == null) return "";
        byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        // 对字节数组Base64编码
        return hl_base64.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static String imgToStr64(String imgFile) throws IOException {
        if (hl_file.isFileExist(imgFile)) {
            return imgToStr64(new FileInputStream(imgFile));
        }
        return "";
    }

    //保存照片
    public static void ImgViewToDcim(ImageView imageView, Context context) {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        bitmapToDcim(bitmap, context);
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }

    /**
     * base64字符串转换成bitmap
     *
     * @param string 字符串
     * @return bitmap
     */
    public static Bitmap stringToBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String addBase64Header(String imgstr) {
        return "data:image/jpeg;base64," + imgstr;
    }

    /**
     * 保存图片到DCIM目录下
     *
     * @param bitmap  图片bitmap
     * @param context 上下文
     * @return 图片路径
     */
    public static String bitmapToDcim(Bitmap bitmap, Context context) {
        //更改的名字
        String imageName = "kou" + hl_time.getNowTimeStr(hl_time.HHmmss) + ".png";
        String headPath = android.os.Environment.getExternalStorageDirectory() + "/DCIM/koutu";
        File headDir = new File(headPath);
        if (!headDir.exists()) {
            headDir.mkdirs();
        }
        System.out.println(headPath + "\n" + headDir);
        FileOutputStream headFos = null;
        File headFile;
        try {
            //重命名并保存
            headFile = new File(headPath, imageName);
            headFile.createNewFile();
            headFos = new FileOutputStream(headFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, headFos);
            headFos.flush();
            // 其次把文件插入到系统图库
//            try {
//                MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                        headFile.getAbsolutePath(), imageName, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            // 最后通知图库更新
            try {
                MediaScannerConnection.scanFile(context, new String[]{headPath}, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + headPath)));
            return headFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (headFos != null) {
                try {
                    headFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap assetsToBitmap(Context context, String assets) throws IOException {
        return BitmapFactory.decodeStream(context.getResources().getAssets().open(assets));
    }

    /**
     * string值转为颜色值
     */
    public static int getColorFromString(String color) {
        return Color.parseColor(color);
    }

    public static void bitmapToFile(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
