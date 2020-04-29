package x.com.hl_fileselect.media;

import java.io.Serializable;
import java.util.List;

public class MediaFolderBean implements Serializable {
    private static final long serialVersionUID = 7523187287116967946L;

    private List<MediaFileBean> mediaFileBeanList;
    private String folderName;

    public List<MediaFileBean> getMediaFileBeanList() {
        return mediaFileBeanList;
    }

    public void setMediaFileBeanList(List<MediaFileBean> mediaFileBeanList) {
        this.mediaFileBeanList = mediaFileBeanList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    private String folderPath;

}
