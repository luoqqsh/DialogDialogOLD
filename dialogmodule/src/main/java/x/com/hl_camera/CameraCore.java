package x.com.hl_camera;

import android.view.SurfaceHolder;

public abstract class CameraCore {
    public int mCameraId = CameraConfig.CAMERA_BACK;

    public abstract void takePhoto();

    public abstract void startPreView();

    public abstract void stopPreView();

    public abstract void releaseCamera();

    //设置预览
    public abstract void setPreviewDisplay(SurfaceHolder surfaceHolder);

    //设置摄像头id
    public abstract void setCameraId(int mCameraId);

    //设置参数
    public abstract void initCameraParam();

    public synchronized void switchCamera() {
        if (mCameraId == CameraConfig.CAMERA_BACK) {
            mCameraId = CameraConfig.CAMERA_FRONT;
        } else {
            mCameraId = CameraConfig.CAMERA_BACK;
        }
        setCameraId(mCameraId);
        startPreView();
    }
}
