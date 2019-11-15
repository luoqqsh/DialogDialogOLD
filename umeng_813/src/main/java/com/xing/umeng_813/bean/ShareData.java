package com.xing.umeng_813.bean;

import android.content.Context;

import com.umeng.socialize.media.UMImage;

import java.io.File;

/**
 * Created by ChenJiaLiang on 2018/4/19.
 * Email:576507648@qq.com
 */

public class ShareData {
    private String content;
    private UMImage umImage;
    private String title;
    private String link;


    private int type;
    private final int TYPE_TEXT = 1;
    private final int TYPE_WEB = 2;
    private final int TYPE_IMAGE = 3;

    public ShareData(String content) {
        type = TYPE_TEXT;
        this.content = content;
    }

    public ShareData(Context context,String imageUrl) {
        type = TYPE_IMAGE;
        this.umImage = new UMImage(context,imageUrl);
    }

    public ShareData(Context context, File imageFile) {
        type = TYPE_IMAGE;
        this.umImage = new UMImage(context,imageFile);
    }

    public ShareData(Context context,String imageUrl, String title, String content, String link) {
        type = TYPE_WEB;
        this.umImage = new UMImage(context,imageUrl);
        this.title = title;
        this.content = content;
        this.link = link;
    }

    public ShareData(Context context,int imageResId, String title, String content, String link) {
        type = TYPE_WEB;
        this.umImage = new UMImage(context,imageResId);
        this.title = title;
        this.content = content;
        this.link = link;
    }

    public UMImage getUmImage() {
        return umImage;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public boolean isText() {
        return type == TYPE_TEXT;
    }

    public boolean isWeb() {
        return type == TYPE_WEB;
    }

    public boolean isImage() {
        return type == TYPE_IMAGE;
    }
}
