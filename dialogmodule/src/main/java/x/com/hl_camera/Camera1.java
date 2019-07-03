package x.com.hl_camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;

import x.com.hl_tools.hl_device;

public class Camera1 {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private Camera.PreviewCallback mPreviewCallback;
    public SurfaceHolder surfaceHolder;
    private ViewGroup viewGroup;

    public Camera1(Activity activity, int mCameraId, ViewGroup viewGroup) {
        this.mCameraId = mCameraId;
//        initCamera(mCameraId);
        setPreView(viewGroup);
    }

    public Camera1(Activity activity, int mCameraId, SurfaceHolder surfaceHolder) {
        this.mCameraId = mCameraId;
        initCamera(mCameraId);
        setPreView(surfaceHolder);
    }

    private synchronized void initCamera(int mCameraId) {
        mCamera = Camera.open(mCameraId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mCamera.enableShutterSound(false);
        }
        mCamera.setPreviewCallbackWithBuffer(callback);
        Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
        parameters.setJpegQuality(100);//设置照片的质量
        parameters.setPreviewSize(WIDTH, HEIGHT);//设置预览尺寸
        parameters.setPictureSize(WIDTH, HEIGHT);//设置照片尺寸
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
//        Camera.Parameters.FOCUS_MODE_AUTO; //自动聚焦模式
//        Camera.Parameters.FOCUS_MODE_INFINITY;//无穷远
//        Camera.Parameters.FOCUS_MODE_MACRO;//微距
//        Camera.Parameters.FOCUS_MODE_FIXED;//固定焦距
        parameters.setZoom(1);
        //设置预览数据格式为nv21
        parameters.setPreviewFormat(ImageFormat.NV21);
        //这是摄像头宽、高
        parameters.setPreviewSize(WIDTH, HEIGHT);
        //数据缓存区
        buffer = new byte[WIDTH * HEIGHT * 3 / 2];
        mCamera.addCallbackBuffer(buffer);
        mCamera.setParameters(parameters);
        setCameraRotation(0);
    }

    public void setPreView(SurfaceHolder surfaceHolder) {
        try {
            if (surfaceHolder != null) {
                mCamera.setPreviewDisplay(surfaceHolder);
            } else {
                setPreView(viewGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPreView(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        SurfaceView mSurfaceView = new SurfaceView(viewGroup.getContext());
        //获取SurfaceHolder对象
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        // 设置 Surface 格式
        // 参数： PixelFormat中定义的 int 值 ,详细参见 PixelFormat.java
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        //保持屏幕常亮（可选）
        mSurfaceHolder.setKeepScreenOn(true);
        // 添加 Surface 的 callback 接口
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder1) {
                //在 Surface 首次创建时被立即调用：获得焦点时。一般在这里开启相机预览
                try {
                    //设置预览画面
                    initCamera(mCameraId);
                    surfaceHolder = surfaceHolder1;
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(surfaceHolder);
                    }
                    startPreView();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                //在 Surface 格式 和 大小发生变化时会立即调用，可以在这个方法中更新 Surface
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //在 Surface 被销毁时立即调用：失去焦点时。一般在这里将相机预览停止销毁
                release();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mSurfaceView, params);
    }

    public static boolean isSupportCamera(Context context) {
        return hl_device.isSupportCamera(context);
    }

    public synchronized void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
//        release();
        setPreView(surfaceHolder);
        startPreView();
    }

    private void setCameraRotation(int rotation) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        //获取摄像头信息
        Camera.getCameraInfo(mCameraId, info);
        //获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        switch (info.facing) {
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                int result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
                mCamera.setDisplayOrientation(result);
                break;
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                result = (info.orientation - degrees + 360) % 360;
                mCamera.setDisplayOrientation(result);
                break;
        }
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void stopPreview() {
        if (mCamera != null) {
            //预览数据回调接口
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
        }
    }

    public void release() {
        if (mCamera != null) {
            stopPreview();
            //释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreView() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }


    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }


    private Camera.PreviewCallback callback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            //SurfaceView 直接预览的  会影响到你的预览吗？
            //Thread.sleep(10_000);
            // data数据依然是倒的
            if (null != mPreviewCallback) {
                mPreviewCallback.onPreviewFrame(bytes, camera);
            }
            camera.addCallbackBuffer(buffer);
        }
    };

}
