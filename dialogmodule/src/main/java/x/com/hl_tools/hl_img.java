package x.com.hl_tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    /**
     * base64字符串转换成bitmap
     *
     * @param string 字符串
     * @return bitmap
     */
    public static Bitmap getBitmapFromStr64(String string) {
        //将字符串转换成Bitmap类型
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            bitmap.setHasAlpha(true);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 增加base64头部信息，应该是要用不到才对
     *
     * @param imgstr 图片base64后的数据
     * @return 图片base64后的数据
     */
    public static String addBase64ImgHeader(String imgstr) {
        return "data:image/jpeg;base64," + imgstr;
    }


    /**
     * #5bc0de值转为颜色值
     */
    public static int getColorFromString(String color) {
        return Color.parseColor(color);
    }

    /**
     * 保存图片到相册目录下
     *
     * @param bitmap  图片bitmap
     * @param context 上下文
     * @return 图片路径
     */
    public static String bitmapToCamera(Bitmap bitmap, Context context) throws IOException {
        return bitmapToCamera(bitmap, context, true);
    }

    public static String bitmapToCamera(Bitmap bitmap, Context context, boolean isUpdateGalley) throws IOException {
        //更改的名字
        String imageName = "dd" + hl_time.getNowTimeStr(hl_time.HHmmss) + ".png";
        bitmapToFile(bitmap, hl_file.CAMERA + File.separator + imageName);
        if (isUpdateGalley) {
            hl_media.updateMedia(context, hl_file.CAMERA + File.separator + imageName);
        }
        return hl_file.CAMERA + File.separator + imageName;
    }

    /**
     * 保存图片到指定文件
     *
     * @param bitmap   图片bitmap
     * @param filePath 保存文件路径
     * @return 图片路径
     */
    public static void bitmapToFile(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把assets里的图片转成bitmap
     *
     * @param context 上下文
     * @param assets  图片名称，根目录下
     * @return bitmap
     */
    public static Bitmap getBitmapFromAssets(Context context, String assets) {
        try {
            InputStream is = context.getAssets().open(assets);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件获取bitmap
     *
     * @return 图片路径
     */
    public static Bitmap getBitmapFromFile(File file) {
        return getBitmapFromPath(file.getAbsolutePath());
    }

    public static Bitmap getBitmapFromPath(String path) {
        if (hl_file.isFileExist(path)) {
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }

    public static Bitmap getBitmapFromResource(Context context, int resource) {
        return BitmapFactory.decodeResource(context.getResources(), resource);
    }

    //把imageview的图片保存到文件，相册目录
    public static void ImgViewToCamera(ImageView imageView, Context context) throws IOException {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageView中的图像
        bitmapToCamera(bitmap, context);
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
