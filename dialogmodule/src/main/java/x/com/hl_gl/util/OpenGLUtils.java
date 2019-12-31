package x.com.hl_gl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import x.com.hl_log.ViseLog;

import static android.opengl.GLES20.GL_TEXTURE_2D;

public class OpenGLUtils {
    /**
     * 错误检查
     *
     * @param glOperation tag
     */
    public static void checkGlError(String glOperation) {
        int errorCode;
        while ((errorCode = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            ViseLog.e(glOperation + "!  code:" + errorCode);
//            Log.e(glOperation, glOperation + "!  code:" + errorCode);
//            throw new RuntimeException(glOperation + ":" + errorCode);
        }
    }

    /**
     * 打印3x3矩阵
     *
     * @param tag               tag
     * @param mProjectionMatrix 矩阵数组
     */
    private void logMatrix33(String tag, float[] mProjectionMatrix) {
        ViseLog.setTag(tag);
        ViseLog.w(mProjectionMatrix[0] + "  " + mProjectionMatrix[1] + "  " + mProjectionMatrix[2] + "\n" +
                mProjectionMatrix[3] + "  " + mProjectionMatrix[4] + "  " + mProjectionMatrix[5] + "\n" +
                mProjectionMatrix[6] + "  " + mProjectionMatrix[7] + "  " + mProjectionMatrix[8] + "\n");
    }

    /**
     * 打印4x4矩阵
     *
     * @param tag               tag
     * @param mProjectionMatrix 矩阵数组
     */
    public static void logMatrix44(String tag, float[] mProjectionMatrix) {
        ViseLog.setTag(tag);
        ViseLog.w(mProjectionMatrix[0] + "  " + mProjectionMatrix[1] + "  " + mProjectionMatrix[2] + "  " + mProjectionMatrix[3] + "\n" +
                mProjectionMatrix[4] + "  " + mProjectionMatrix[5] + "  " + mProjectionMatrix[6] + "  " + mProjectionMatrix[7] + "\n" +
                mProjectionMatrix[8] + "  " + mProjectionMatrix[9] + "  " + mProjectionMatrix[10] + "  " + mProjectionMatrix[11] + "\n" +
                mProjectionMatrix[12] + "  " + mProjectionMatrix[13] + "  " + mProjectionMatrix[14] + "  " + mProjectionMatrix[15]);
    }

    /**
     * 获取原始矩阵
     *
     * @return 原始矩阵
     */
    public static float[] getOriginalMatrix() {
        return new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    /**
     * 创建FBO纹理并配置(待测试)
     *
     * @param mFrameBuffers  mFrameBuffers
     * @param mFrameTextures mFrameTextures
     * @param canvasWidth    宽
     * @param canvasHeight   高
     */
    public static void glGenTextures(int[] mFrameBuffers, int[] mFrameTextures, int canvasWidth, int canvasHeight) {
        // 1、创建几个fbo 2、保存fbo id的数据 3、从这个数组的第几个开始保存
        GLES20.glGenFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
        glGenTextures(mFrameTextures);
        //创建一个 2d的图像
        // 目标 2d纹理+等级 + 格式 +宽、高+ 格式 + 数据类型(byte) + 像素数据
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameTextures[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, canvasWidth, canvasHeight,
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        // 让fbo与纹理绑定起来 ， 后续的操作就是在操作fbo与这个纹理上了
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, mFrameTextures[0], 0);
        //解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    /**
     * 创建纹理并配置:添加图片
     *
     * @param textures 纹理id数组(新建的)
     */
    public static void glGenTextures(int[] textures) {
        glGenTextures(textures, null);
    }

    /**
     * 创建纹理并配置:添加图片
     *
     * @param textures 纹理id数组(新建的)
     * @param bitmap   bitmap
     * @return
     */
    public static void glGenTextures(int[] textures, Bitmap bitmap) {
        //创建
        GLES20.glGenTextures(textures.length, textures, 0);
        //配置
        for (int i = 0; i < textures.length; i++) {
            //bind 就是绑定 ，表示后续的操作就是在这一个 纹理上进行
            // 后面的代码配置纹理，就是配置bind的这个纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            /**
             * 过滤参数
             *  当纹理被使用到一个比他大 或者比他小的形状上的时候 该如何处理
             */
            // 放大
            // GLES20.GL_LINEAR  : 使用纹理中坐标附近的若干个颜色，通过平均算法 进行放大
            // GLES20.GL_NEAREST : 使用纹理坐标最接近的一个颜色作为放大的要绘制的颜色
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

            /*设置纹理环绕方向*/
            //纹理坐标 一般用st表示，其实就是x y
            //纹理坐标的范围是0-1。超出这一范围的坐标将被OpenGL根据GL_TEXTURE_WRAP参数的值进行处理
            //GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_T 分别为x，y方向。
            //GL_REPEAT:平铺
            //GL_MIRRORED_REPEAT: 纹理坐标是奇数时使用镜像平铺
            //GL_CLAMP_TO_EDGE: 坐标超出部分被截取成0、1，边缘拉伸
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            if (bitmap != null) {
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            }
            //解绑
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    /**
     * 清除画布
     */
    public static void clearColor() {
        clearColor(1, 0.5f, 0, 0);
    }

    /**
     * 清除画布颜色'深度
     */
    public static void clearColorDepth() {
        GLES20.glClearColor(1, 0.5f, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * 指定画布颜色
     */
    public static void clearColor(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    //********************************************************************************************//
    public static void copyAssets2SdCard(Context context, String src, String dst) {
        try {
            File file = new File(dst);
            if (!file.exists()) {
                InputStream is = context.getAssets().open(src);
                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int createTextureObject(int textureTarget) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int texId = textures[0];
        GLES20.glBindTexture(textureTarget, texId);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        return texId;
    }

    public static int genTexture() {
        return genTexture(GLES20.GL_TEXTURE_2D);
    }

    public static int genTexture(int textureType) {
        int[] genBuf = new int[1];
        GLES20.glGenTextures(1, genBuf, 0);
        GLES20.glBindTexture(textureType, genBuf[0]);

        // Set texture default draw parameters
        if (textureType == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) {
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        } else {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        }

        return genBuf[0];
    }

    public static int genLutTexture() {
        int[] genBuf = new int[1];
        GLES20.glGenTextures(1, genBuf, 0);
        GLES20.glBindTexture(GL_TEXTURE_2D, genBuf[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        return genBuf[0];
    }
}
