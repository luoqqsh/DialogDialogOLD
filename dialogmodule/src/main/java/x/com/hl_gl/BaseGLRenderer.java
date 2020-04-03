package x.com.hl_gl;

import android.opengl.GLSurfaceView;

public abstract class BaseGLRenderer implements GLSurfaceView.Renderer {

    protected static final int FLOAT_SIZE_BYTES = 4;

    protected static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;

    protected static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;

    protected static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

    protected static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    protected final float[] mTriangleVerticesData = {
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            -1.0f, 0.0f, 1.0f,
            0.0f, -1.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f,};
}
