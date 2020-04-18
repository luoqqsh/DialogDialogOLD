package com.xing.hhplayer.common.TypeBean;

/**
 * video的一些默认配置
 * Created by shuyu on 2016/12/7.
 */

public class PlayerViewType {
    //默认显示比例，按比例全屏显示
    public final static int SCREEN_SCALE_DEFAULT = 0;
    //4:3
    public final static int SCREEN_SCALE_4_3 = 1;
    //16:9
    public final static int SCREEN_SCALE_16_9 = 2;
    //1:1
    public final static int SCREEN_SCALE_1_1 = 3;
    //拉伸全屏
    public final static int SCREEN_SCALE_FULL = 4;

    //正常大小
    public final static int SCREEN_DEFAULT = 0;
    //窗口播放
    public final static int SCREEN_WINDOW = 1;
    //全屏播放
    public final static int SCREEN_FULL = 2;

    public static String getScaleStr(int screenScaleType) {
        switch (screenScaleType) {
            case 0:
                return "SCREEN_SCALE_DEFAULT";
            case 1:
                return "SCREEN_SCALE_4_3";
            case 2:
                return "SCREEN_SCALE_16_9";
            case 3:
                return "SCREEN_SCALE_1_1";
            case 4:
                return "SCREEN_SCALE_FULL";
            default:
                return "";
        }
    }

    public static float getScaleByScreenType(int screenScaleType) {
        switch (screenScaleType) {
            case SCREEN_SCALE_4_3:
                return 4 * 1f / 3;
            case SCREEN_SCALE_16_9:
                return 16 * 1f / 9;
            case SCREEN_SCALE_1_1:
                return 1;
            default:
                return 0;
        }
    }

    //全屏裁减显示，为了显示正常 CoverImageView 建议使用FrameLayout作为父布局
    public final static int SCREEN_TYPE_FULL = 4;
    public final static int SCREEN_TYPE_FULL2 = 3;

    //默认显示比例
    public final static int SCREEN_TYPE_DEFAULT = 0;

    //16:9
    public final static int SCREEN_TYPE_16_9 = 1;

    //4:3
    public final static int SCREEN_TYPE_4_3 = 2;

    //全屏拉伸显示，使用这个属性时，surface_container建议使用FrameLayout
    public final static int SCREEN_MATCH_FULL = -4;

    //显示比例类型
    private static int TYPE = SCREEN_TYPE_DEFAULT;

    //硬解码标志
    private static boolean MEDIA_CODEC_FLAG = false;

    //是否使用硬解码优化
    private static boolean sTextureMediaPlay = false;


    /**
     * 使能硬解码，播放前设置
     */
    public static void enableMediaCodec() {
        MEDIA_CODEC_FLAG = true;
    }

    /**
     * 关闭硬解码，播放前设置
     */
    public static void disableMediaCodec() {
        MEDIA_CODEC_FLAG = false;
    }

    /**
     * 使能硬解码渲染优化
     */
    public static void enableMediaCodecTexture() {
        sTextureMediaPlay = true;
    }

    /**
     * 关闭硬解码渲染优化
     */
    public static void disableMediaCodecTexture() {
        sTextureMediaPlay = false;
    }

    /**
     * 是否开启硬解码
     */
    public static boolean isMediaCodec() {
        return MEDIA_CODEC_FLAG;
    }

    /**
     * 是否开启硬解码渲染优化
     */
    public static boolean isMediaCodecTexture() {
        return sTextureMediaPlay;
    }

    public static int getShowType() {
        return TYPE;
    }

    /**
     * 设置显示比例,注意，这是全局生效的
     */
    public static void setShowType(int type) {
        TYPE = type;
    }
}
