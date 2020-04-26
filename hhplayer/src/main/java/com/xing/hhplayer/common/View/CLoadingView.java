package com.xing.hhplayer.common.View;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.View.loading.PlayerLoadingView;

public class CLoadingView extends FrameLayout implements View.OnClickListener {

    private PlayerLoadingView loading;
    private ViewGroup loading_layout;

    public CLoadingView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.player_cloading, this);
        loading_layout = findViewById(R.id.loadingLayout);
        loading = findViewById(R.id.loadingView);
    }

    @Override
    public void onClick(View view) {

    }

    public void startLoading() {
        loading_layout.setVisibility(VISIBLE);
        loading.setVisibility(VISIBLE);
    }

    public void stopLoading() {
        loading_layout.setVisibility(GONE);
        loading.setVisibility(GONE);
    }
}
