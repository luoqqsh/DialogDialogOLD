package x.com.hl_model;

import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;


import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class MediaFileBean implements Serializable, Comparable<MediaFileBean> {

    public MediaFileBean() {
    }

    //列表操作
    private boolean isCheck = false;//是否选中，列表选择状态用
    private int tag;//视频标记
    //文件
    private String mediaPath;//路径
    private String thumbPath;//封面
    private String mediaPathUri;//路径uri
    private int mediaId;//文件资源id
    private long size;//文件大小
    private long duration = 0;//时长
    private long date = 0;//文件修改日期
    private int iconId;//文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片
    private String fileName;//文件名
    private String mimeType;//文件类型

    //通过文件类型得到相应文件的集合
    public MediaFileBean(String mediaPath, int iconId) {
        this.mediaPath = mediaPath;
        this.iconId = iconId;
    }

    //音乐
    private String title;//歌曲名
    private String album;//所属专辑
    private int albumId;//所属专辑id
    private String artist;//艺术家(作者)
    private String type;//类型
    private String year;//发行日期
    private String pinyin;

    //视频
    private String resolution;// 分辨率

    //其他控制
    private boolean playing = false;//是否正在播放

    //获取视频列表
    public MediaFileBean(int mediaId, String mediaPath, String resolution, long size, long date, long duration) {
        this.mediaId = mediaId;
        this.mediaPath = mediaPath;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    //获取系统视频库里的列表
    public MediaFileBean(int mediaId, String title, String album, String artist, String mimeType, String mediaPath, long size, long duration) {
        this.mediaId = mediaId;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.mimeType = mimeType;
        this.mediaPath = mediaPath;
        this.size = size;
        this.duration = duration;
    }

    //本地视频列表中使用
    public MediaFileBean(String mediaPath, long size, long duration, int tag) {
        this.mediaPath = mediaPath;
        this.size = size;
        this.duration = duration;
        this.tag = tag;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        this.fileName = new File(mediaPath).getName();
    }

    public String getMediaPathUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(mediaId)).build().toString();
    }

    public void setMediaPathUri(String mediaPathUri) {
        this.mediaPathUri = mediaPathUri;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getFileName() {
        if (TextUtils.isEmpty(fileName)) {
            fileName = new File(mediaPath).getName();
        }
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getSizeStr() {
        if (size > 1024 * 1024) {
            return size / 1024 / 1024 + "M";
        } else if (size > 1024) {
            return size / 1024 + "KB";
        } else if (size > 0) {
            return size + "B";
        } else {
            return "未知";
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getTag() {
        return tag;
    }

    @Override
    public int compareTo(MediaFileBean o) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaFileBean that = (MediaFileBean) o;
        return duration == that.duration &&
                Objects.equals(mediaPath, that.mediaPath) &&
                Objects.equals(thumbPath, that.thumbPath) &&
                Objects.equals(mediaPathUri, that.mediaPathUri) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(title, that.title);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(mediaPath, thumbPath, mediaPathUri, duration, fileName, title);
    }
}
