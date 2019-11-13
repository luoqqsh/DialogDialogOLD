package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TypeBean.PlayerPrePareStateType;
import com.xing.hhplayer.common.TypeBean.PlayerStateType;
import com.xing.hhplayer.common.base.ControlViewInterface;

public class HHThumbView extends FrameLayout implements ControlViewInterface {

    private ImageView defaultThumb;

    public HHThumbView(@NonNull Context context, HHPlayer3 hhPlayer3) {
        super(context);
        hhPlayer3.setCvInterface(this);
        initView(context);
    }

    public HHThumbView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHThumbView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.hhplayer_thumb, this);
        defaultThumb = findViewById(R.id.defaultThumb);
    }

    public ImageView getDefaultThumb() {
        return defaultThumb;
    }


    @Override
    public void getPlayState(PlayerStateType playerStateType) {
    }

    @Override
    public void getPrePareState(PlayerPrePareStateType playerPrePareStateType) {

    }

    @Override
    public void changeProgress(final long time) {

    }
}
