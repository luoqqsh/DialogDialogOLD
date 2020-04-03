package com.xing.hhplayer.common.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.tools.MeasureHelper;

public class HHGLSurfaceView extends GLSurfaceView {
    private HHSurfaceStateListener surfaceListener;
    private MeasureHelper measureHelper;

    public HHGLSurfaceView(Context context) {
        super(context);
    }


    private void init(Context context) {
        setEGLContextClientVersion(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
        setMeasuredDimension(measureHelper.getMeasuredWidth(), measureHelper.getMeasuredHeight());
    }
}
