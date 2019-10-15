package x.com.hl_tools.DialogTest.phone.bean;

import java.io.File;

public class MediaFileBean {
    public MediaFileBean() {
    }

    private String mediaPath;//路径
    private String thumbPath;//封面
    private String mediaPathUri;//路径uri
    private int mediaId;//文件资源id
    private long size;//文件大小
    private long duration = 0;//时长
    private long date = 0;//文件修改日期
    private int iconId;//文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片
    private String fileName;//歌曲文件名

    //通过文件类型得到相应文件的集合
    public MediaFileBean(String mediaPath, int iconId) {
        this.mediaPath = mediaPath;
        this.iconId = iconId;
        this.fileName = new File(mediaPath).getName();
    }

    private String title;//歌曲名
    private String album;//所属专辑
    private int albumId;//所属专辑id
    private String artist;//艺术家(作者)
    private String type;//类型
    private String year;//发行日期
    private String pinyin;

    private String resolution;// 分辨率


    //获取视频列表
    public MediaFileBean(int mediaId, String mediaPath, String resolution, long size, long date, long duration) {
        this.mediaId = mediaId;
        this.mediaPath = mediaPath;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
        this.fileName = new File(mediaPath).getName();
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
        return mediaPathUri;
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
}
