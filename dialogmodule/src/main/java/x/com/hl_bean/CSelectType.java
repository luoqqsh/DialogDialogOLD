package x.com.hl_bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author:ChenJiaLiang
 * Date:2019/11/13
 * Description: 解锁类型
 */
@Retention(RetentionPolicy.SOURCE)
public @interface CSelectType {
    int files = 0;//文件
    int images = 1;//图片
    int videos = 2;//视频
    int music = 3;//音乐
    int photo = 4;//拍照
}
