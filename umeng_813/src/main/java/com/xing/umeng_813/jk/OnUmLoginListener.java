package com.xing.umeng_813.jk;


import com.xing.umeng_813.bean.OauthInfo;

/**
 * Created by ChenJiaLiang on 2018/4/20.
 * Email:576507648@qq.com
 */

public interface OnUmLoginListener {
    void onStart();
    void onCancel();
    void onSuccess(OauthInfo oauthInfo);
    void onFail(String error);
}
