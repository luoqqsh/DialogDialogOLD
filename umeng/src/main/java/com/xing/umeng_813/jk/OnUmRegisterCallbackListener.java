package com.xing.umeng_813.jk;

public interface OnUmRegisterCallbackListener {
    //成功返回token，失败返回错误信息
    void onListener(boolean isSuccess, String token, String message);
}
