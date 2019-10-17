package com.xing.umeng_813.jk;

/**
 * Created by ChenJiaLiang on 2018/4/20.
 * Email:576507648@qq.com
 */

public interface OnUmShareListener {
    void onStart();
    void onCancel();
    void onSuccess();
    void onFail(String error);
}
