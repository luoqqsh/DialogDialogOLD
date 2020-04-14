package com.jiujie.glide;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.InputStream;

import static com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.TIMEOUT;

/**
 * Glide 4.x 自定义GlideModel
 * 这里指定Picture类型的Model由用户处理
 */
public class MyModelLoader implements ModelLoader<Picture, InputStream> {

    public MyModelLoader() {
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(Picture model, int width, int height,
                                               Options options) {
        int timeout = options.get(TIMEOUT);
        GlideUrl glideUrl = new GlideUrl(model.getFilePath());
        return new LoadData<>(glideUrl, new HttpInputFetcher(glideUrl, timeout));
    }

    @Override
    public boolean handles(Picture s) {
        return true;
    }

}

