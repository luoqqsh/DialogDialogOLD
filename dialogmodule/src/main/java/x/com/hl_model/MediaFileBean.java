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

    //基本使用
    public MediaFileBean(File file) {
        this.filePath = file.getAbsolutePath();
        this.fileName = file.getName();
        this.fileSize = file.length();
    }

    //列表操作
    private boolean isCheck = false;//是否选中，列表选择状态用
    private int tag;//视频标记
    //文件
    private String filePath;//路径
    private String filePathUri;//路径uri
    private String thumbPath;//封面
    private int mediaId;//文件资源id
    private long fileSize;//文件大小
    private long mediaDuration = 0;//时长
    private long fileDate = 0;//文件修改日期
    private int iconId;//文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片
    private String fileName;//文件名
    private String fileMimeType;//文件类型

    //通过文件类型得到相应文件的集合
    public MediaFileBean(String filePath, int iconId) {
        this.filePath = filePath;
        this.iconId = iconId;
    }

    //音乐
    private String musicName;//歌曲名
    private String musicAlbum;//所属专辑
    private int musicAlbumId;//所属专辑id
    private String artist;//艺术家(作者)
    private String type;//类型
    private String year;//发行日期
    private String pinyin;

    //视频
    private String resolution;// 分辨率

    //其他控制
    private boolean playing = false;//是否正在播放

    //获取视频列表
    public MediaFileBean(int mediaId, String filePath, String resolution, long fileSize, long fileDate, long mediaDuration) {
        this.mediaId = mediaId;
        this.filePath = filePath;
        this.resolution = resolution;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.mediaDuration = mediaDuration;
    }

    //获取系统视频库里的列表
    public MediaFileBean(int mediaId, String musicName, String musicAlbum, String artist, String fileMimeType, String filePath, long fileSize, long mediaDuration) {
        this.mediaId = mediaId;
        this.musicName = musicName;
        this.musicAlbum = musicAlbum;
        this.artist = artist;
        this.fileMimeType = fileMimeType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaDuration = mediaDuration;
    }

    //本地视频列表中使用
    public MediaFileBean(String filePath, long fileSize, long mediaDuration, int tag) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaDuration = mediaDuration;
        this.tag = tag;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.fileName = new File(filePath).getName();
    }

    public String getFilePathUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(mediaId)).build().toString();
    }

    public void setFilePathUri(String filePathUri) {
        this.filePathUri = filePathUri;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(long mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public long getFileDate() {
        return fileDate;
    }

    public void setFileDate(long fileDate) {
        this.fileDate = fileDate;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getFileName() {
        if (TextUtils.isEmpty(fileName)) {
            fileName = new File(filePath).getName();
        }
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    public int getMusicAlbumId() {
        return musicAlbumId;
    }

    public void setMusicAlbumId(int musicAlbumId) {
        this.musicAlbumId = musicAlbumId;
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
        if (fileSize > 1024 * 1024) {
            return fileSize / 1024 / 1024 + "M";
        } else if (fileSize > 1024) {
            return fileSize / 1024 + "KB";
        } else if (fileSize > 0) {
            return fileSize + "B";
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

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
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
        return mediaDuration == that.mediaDuration &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(thumbPath, that.thumbPath) &&
                Objects.equals(filePathUri, that.filePathUri) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(musicName, that.musicName);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(filePath, thumbPath, filePathUri, mediaDuration, fileName, musicName);
    }
}
