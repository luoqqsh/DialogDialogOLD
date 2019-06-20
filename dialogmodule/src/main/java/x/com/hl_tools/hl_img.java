package x.com.hl_tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class hl_img {
    // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public static String imgToStr64(String imgFile) {
        InputStream in;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码

        if (data != null) {
            return hl_base64.encode(data);// 返回Base64编码过的字节数组字符串
        } else {
            return "";
        }
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
     * 保存方法
     */
    public static void bitmapToDcim(Bitmap bitmap, Context context) {
        //更改的名字
        String imageName = new Date() + ".jpg";

        String headPath = android.os.Environment.getExternalStorageDirectory() +
                "/DCIM/Screenshots";

        File headDir = new File(headPath);
        if (!headDir.exists()) {
            headDir.mkdirs();
        }
        System.out.println(headPath + "\n" + headDir);
        FileOutputStream headFos = null;
        File headFile = null;
        try {
            //重命名并保存
            headFile = new File(headPath, imageName);
            headFile.createNewFile();

            headFos = new FileOutputStream(headFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, headFos);
            headFos.flush();
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        headFile.getAbsolutePath(), imageName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + headPath)));

        } catch (Exception e) {
            e.printStackTrace();
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

    /*
     * Drawable → Bitmap
     */
    private static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /*
     * Bitmap → Drawable
     */
    @SuppressWarnings("deprecation")
    private static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        BitmapDrawable bd = new BitmapDrawable(bm);
        bd.setTargetDensity(bm.getDensity());
        return new BitmapDrawable(bm);
    }

    /*
     * Bitmap → byte[]
     */
    private static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * byte[] → Bitmap
     */
    private static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
