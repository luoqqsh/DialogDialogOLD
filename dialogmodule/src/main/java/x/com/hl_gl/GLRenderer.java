package x.com.hl_gl;

import android.content.Context;
import android.opengl.GLES30;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import x.com.hl_dialog.R;
import x.com.hl_gl.renderer.BitmapDrawer;
import x.com.hl_gl.renderer.TriangleDrawer;
import x.com.hl_gl.util.OpenGLUtils;
import x.com.hl_tools.hl_bitmap;

public class GLRenderer extends BaseGLRenderer {
    private TriangleDrawer mTriangle;
    private BitmapDrawer mBitmapDrawer;
    private Context context;

    public GLRenderer() {
    }

    public GLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        OpenGLUtils.clearColor();
//        mTriangle = new TriangleDrawer();
        mBitmapDrawer = new BitmapDrawer(hl_bitmap.getBitmapFromDrawableId(context, R.mipmap.img_dingbu));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //OpenGL在GLSurfaceView中的绘制区域
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        mTriangle.draw();
        mBitmapDrawer.onDraw();
    }

}
