package x.com.hl_camera;

import android.view.SurfaceHolder;

public abstract class CameraCore {
    public int mCameraId = CameraConfig.CAMERA_BACK;

    public abstract void initCamera();

    public abstract void takePhoto();

    public abstract void startPreView();

    public abstract void stopPreView();

    public abstract void releaseCamera();

    public abstract void setPreviewDisplay(SurfaceHolder surfaceHolder);

    public abstract void setCameraId(int mCameraId);

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
