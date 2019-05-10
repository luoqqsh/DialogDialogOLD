package x.com.hl_gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLView extends GLSurfaceView {
    private Context mContext;
    private GLRenderer mRenderer;

    public GLView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        mRenderer = new GLRenderer();
        setRenderer(mRenderer);
        //自动调用，每秒60次
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //按需调用
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
