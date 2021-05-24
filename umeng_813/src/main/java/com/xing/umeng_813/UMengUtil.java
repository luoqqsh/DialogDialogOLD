package com.xing.umeng_813;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.vise.log.ViseLog;
import com.xing.umeng_813.bean.OauthInfo;
import com.xing.umeng_813.bean.ShareData;
import com.xing.umeng_813.bean.UmengConfig;
import com.xing.umeng_813.jk.OnUmLoginListener;
import com.xing.umeng_813.jk.OnUmRegisterCallbackListener;
import com.xing.umeng_813.jk.OnUmShareListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信必须要有客户端，QQ和新浪，可以用网页实现分享和登录
 * 初始化方法有变，其他没变
 * Created by LuoYida on 2019/9/27.
 * Email:luoqqsh@qq.com
 */
public class UMengUtil {

    private static UMengUtil uMengUtil;
    private Context context;
    private UmengConfig config;
    private final String TAG = "Umeng";
    private OnUmRegisterCallbackListener onUmRegisterCallbackListener;
    private UmengNotificationClickHandler notificationClickHandler;

    public void setOnUmRegisterCallbackListener(OnUmRegisterCallbackListener onUmRegisterCallbackListener) {
        this.onUmRegisterCallbackListener = onUmRegisterCallbackListener;
    }

    public void setNotificationClickHandler(UmengNotificationClickHandler notificationClickHandler) {
        this.notificationClickHandler = notificationClickHandler;
    }

    public static UMengUtil instance() {
        if (uMengUtil == null) {
            uMengUtil = new UMengUtil();
        }
        return uMengUtil;
    }

    private UMengUtil() {
    }

    //UmengConfig-继承这个类，重写需要的key及其他配置信息
    public void init(Context context, UmengConfig umengConfig) {
        this.context = context;
        this.config = umengConfig;
        //统计
        UMConfigure.setLogEnabled(config.isDebug);//友盟日志开关
        UMConfigure.setEncryptEnabled(config.encryptEnabled);//日志加密
        MobclickAgent.setSessionContinueMillis(config.sessionContinueMillis);//退出应用，再过多长时间进来表示不同的启动
//        MobclickAgent.openActivityDurationTrack(true);//页面统计--手动统计

        //推送
        UMConfigure.init(context, config.APPKEY, config.CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, config.PUSH_SECRET);
        if (config.isInitPush()) {
            PushAgent mPushAgent = PushAgent.getInstance(context);
            PushAgent.DEBUG = config.isDebug;
            mPushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String deviceToken) {
                    Log.d(TAG, "推送注册成功 deviceToken:" + deviceToken);
                    if (onUmRegisterCallbackListener != null) {
                        onUmRegisterCallbackListener.onListener(true, deviceToken, "");
                    }
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.e(TAG, "推送注册失败" + "onFailure s:" + s + ",s1:" + s1);
                    if (onUmRegisterCallbackListener != null) {
                        onUmRegisterCallbackListener.onListener(false, s, s1);
                    }
                }
            });
            if (notificationClickHandler == null) {
                notificationClickHandler = new UmengNotificationClickHandler() {
                    @Override
                    public void dealWithCustomAction(Context context, UMessage uMessage) {
                        super.dealWithCustomAction(context, uMessage);
                        Log.d(TAG, "msg.custom:" + uMessage.custom);
                    }
                };
            }
            mPushAgent.setNotificationClickHandler(notificationClickHandler);
        }
        //分享
        if (config.isInitWX()) {
            PlatformConfig.setWeixin(config.WX_ID, config.WX_SECRET);
        }
        if (config.isInitQQ()) {
            PlatformConfig.setQQZone(config.QQ_ID, config.QQ_KEY);
        }
        if (config.isInitSina()) {
            PlatformConfig.setSinaWeibo(config.SINA_ID, config.SINA_SECRET, config.SINA_REDIREC_URL);
        }
        UMShareAPI.init(context, config.APPKEY);
        UMShareAPI.get(context);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 分享
     */
    public void share(Activity activity, UmShareType thirdShareType, ShareData shareData, final OnUmShareListener shareListener) {
        SHARE_MEDIA shareMedia = null;
        switch (thirdShareType) {
            case QQ:
                shareMedia = SHARE_MEDIA.QQ;
                break;
            case QQZone:
                shareMedia = SHARE_MEDIA.QZONE;
                break;
            case WeiXin:
                shareMedia = SHARE_MEDIA.WEIXIN;
                break;
            case WeiXinCircle:
                shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
        }
        ShareAction shareAction = new ShareAction(activity);
        shareAction.setPlatform(shareMedia);//传入平台
        if (shareData.isText()) {
            shareAction.withText(shareData.getContent());
        } else if (shareData.isWeb()) {
            UMWeb web = new UMWeb(shareData.getLink());
            web.setTitle(shareData.getTitle());//标题
            web.setThumb(shareData.getUmImage());  //缩略图
            web.setDescription(shareData.getContent());//描述
            shareAction.withMedia(web);
        } else if (shareData.isImage()) {
            shareAction.withMedia(shareData.getUmImage());
        }
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (shareListener != null) shareListener.onStart();
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (shareListener != null) shareListener.onSuccess();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (shareListener != null) shareListener.onFail(throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (shareListener != null) shareListener.onCancel();
            }
        }).share();
    }

    public void statics(String id, String key, String values) {
        MobclickAgent.onEvent(context, id, getParaMap(key, values));
    }

    private Map<String, String> getParaMap(String key, String values) {
        Map<String, String> m = new HashMap();
        m.put(key, values);
        return m;
    }

    public void loginQQ(Activity activity, OnUmLoginListener loginListener) {
        login(activity, UmLoginType.QQ, loginListener);
    }

    public void loginWX(Activity activity, OnUmLoginListener loginListener) {
        login(activity, UmLoginType.WeiXin, loginListener);
    }

    public void loginSina(Activity activity, OnUmLoginListener loginListener) {
        login(activity, UmLoginType.SINA, loginListener);
    }

    private void login(Activity activity, UmLoginType thirdLoginType, OnUmLoginListener umLoginListener) {
        SHARE_MEDIA shareMedia = null;
        switch (thirdLoginType) {
            case QQ:
                shareMedia = SHARE_MEDIA.QQ;
                break;
            case WeiXin:
                shareMedia = SHARE_MEDIA.WEIXIN;
                break;
            case SINA:
                shareMedia = SHARE_MEDIA.SINA;
                break;
        }
        saveLastLoginType(activity, thirdLoginType);
        UMShareAPI.get(activity).getPlatformInfo(activity, shareMedia, getLoginListener(umLoginListener));
    }

    private void saveLastLoginType(Activity activity, UmLoginType thirdLoginType) {
        SharedPreferences sp = activity.getSharedPreferences("third_login", Context.MODE_PRIVATE);
        sp.edit().putString("loginType", thirdLoginType.toString()).apply();
    }

    private UmLoginType getLastLoginType(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("third_login", Context.MODE_PRIVATE);
        String loginType = sp.getString("loginType", UmLoginType.QQ.toString());
        if (loginType != null) {
            if (loginType.equals(UmLoginType.QQ.toString())) {
                return UmLoginType.QQ;
            } else if (loginType.equals(UmLoginType.WeiXin.toString())) {
                return UmLoginType.WeiXin;
            } else if (loginType.equals(UmLoginType.SINA.toString())) {
                return UmLoginType.SINA;
            }
        }
        return UmLoginType.QQ;
    }

    public String getRegistrationId() {
        return PushAgent.getInstance(context).getRegistrationId();
    }

    public void logoff(final Activity activity) {
        SHARE_MEDIA mediaType = getMediaType(getLastLoginType(activity));
        UMShareAPI.get(activity).deleteOauth(activity, mediaType, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (config.isDebug) Log.e(TAG, "logoff start");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (config.isDebug) Log.e(TAG, "logoff onComplete");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (config.isDebug) Log.e(TAG, "logoff onError " + throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (config.isDebug) Log.e(TAG, "logoff onCancel ");
            }
        });
    }

    private SHARE_MEDIA getMediaType(UmLoginType thirdLoginType) {
        SHARE_MEDIA shareMedia = null;
        switch (thirdLoginType) {
            case QQ:
                shareMedia = SHARE_MEDIA.QQ;
                break;
            case WeiXin:
                shareMedia = SHARE_MEDIA.WEIXIN;
                break;
            case SINA:
                shareMedia = SHARE_MEDIA.SINA;
                break;
        }
        return shareMedia;
    }

    private UMAuthListener getLoginListener(final OnUmLoginListener umLoginListener) {
        return new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (umLoginListener != null) umLoginListener.onStart();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (config.isDebug) {
                    for (String key : map.keySet()) {
                        Log.e(TAG, "onComplete map key:" + key + "-" + map.get(key));
                    }
                }
                OauthInfo oauthInfo = new OauthInfo();
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    oauthInfo.setType("weixin");
                    //这边判断是否是微信，如果是微信的话单独处理
                    //openid为了和QQ微博保持一致，所以openid传uid（以后的用户统一以uid作为用户唯一标识）:（友盟开发说明：uid （6.2以前用unionid）用户id）
                    //增加一个change_openid参数，这个参数是对应openid
                    //两个参数都传，由服务端判断用户哪个id最早注册就使用哪个id
                    oauthInfo.setToken(map.get("accessToken"));

                    if (map.get("uid") != null && !TextUtils.isEmpty(map.get("uid"))) {
                        oauthInfo.setUid(map.get("uid"));
                    } else if (map.get("unionid") != null && !TextUtils.isEmpty(map.get("unionid"))) {
                        oauthInfo.setUid(map.get("unionid"));
                    }
                    oauthInfo.setOpenid(map.get("openid"));
                    oauthInfo.setNickname(map.get("name"));
                    oauthInfo.setExpires_in(map.get("expiration"));
                    oauthInfo.setGender(map.get("gender"));
                    oauthInfo.setRegistration(UMengUtil.instance().getRegistrationId());
                    oauthInfo.setAvatar(map.get("iconurl"));
                } else if (share_media == SHARE_MEDIA.QQ) {
                    oauthInfo.setType("qq");
                    oauthInfo.setToken(map.get("accessToken"));
                    oauthInfo.setOpenid(map.get("uid"));
                    oauthInfo.setNickname(map.get("name"));
                    oauthInfo.setExpires_in(map.get("expiration"));
                    oauthInfo.setGender(map.get("gender"));
                    oauthInfo.setRegistration(UMengUtil.instance().getRegistrationId());
                    oauthInfo.setAvatar(map.get("iconurl"));
                } else if (share_media == SHARE_MEDIA.SINA) {
                    oauthInfo.setType("sina");
                    oauthInfo.setToken(map.get("accessToken"));
                    oauthInfo.setOpenid(map.get("uid"));
                    oauthInfo.setNickname(map.get("name"));
                    oauthInfo.setExpires_in(map.get("expiration"));
                    oauthInfo.setGender(map.get("gender"));
                    oauthInfo.setRegistration(UMengUtil.instance().getRegistrationId());
                    oauthInfo.setAvatar(map.get("iconurl"));
                }
                if (umLoginListener != null) umLoginListener.onSuccess(oauthInfo);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (umLoginListener != null) umLoginListener.onFail(throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (umLoginListener != null) umLoginListener.onCancel();
            }
        };
    }
}
