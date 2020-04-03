package x.com.hl_gl;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import x.com.hl_gl.shape.Triangle;
import x.com.hl_gl.util.OpenGLUtils;

public class GLRenderer extends BaseGLRenderer {
    private Triangle mTriangle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        OpenGLUtils.clearColor();
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mTriangle.draw();
    }

}
