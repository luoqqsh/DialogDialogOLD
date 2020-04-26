package com.xing.hhplayer.common.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.hhplayer.R;

public class CThumbView extends FrameLayout {
    private ImageView defaultThumb;
    private Bitmap bitmap;

    public CThumbView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CThumbView(@NonNull Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.player_cthumb, this);
        defaultThumb = findViewById(R.id.defaultThumb);
        if (bitmap != null) {
            defaultThumb.setImageBitmap(bitmap);
        }
    }

    public ImageView getDefaultThumbView() {
        return defaultThumb;
    }
}
