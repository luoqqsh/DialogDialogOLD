package x.com.hl_tools.DialogTest.phone.bean;


public class Music {
    private String fileName;//歌曲文件名
    private String title;//歌曲名
    private String path;//路径
    private String album;//所属专辑
    private int albumId;//所属专辑id
    private String artist;//艺术家(作者)
    private long size;//文件大小
    private int duration;//时长
    private int MusicId;//id
    private String type;//类型
    private String year;//发行日期
    private String pinyin;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMusicId() {
        return MusicId;
    }

    public void setMusicId(int musicId) {
        this.MusicId = musicId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
