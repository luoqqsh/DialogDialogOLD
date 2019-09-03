package x.com.dialogdialog.face.model;

public class OnLineResponse {
    private String labelmap;
    private String scoremap;
    private String foreground;
    private String error_msg;
    private int error_code;

    public String getLabelmap() {
        return labelmap;
    }

    public void setLabelmap(String labelmap) {
        this.labelmap = labelmap;
    }

    public String getScoremap() {
        return scoremap;
    }

    public void setScoremap(String scoremap) {
        this.scoremap = scoremap;
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    public int getErrorCode() {
        return error_code;
    }

    public void setErrorCode(int errorCode) {
        this.error_code = errorCode;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
