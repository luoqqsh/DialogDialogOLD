package x.com.hl_tools.hl_interface;

import java.io.File;


public interface DownListener {
    void onsuccess(File file);

    void faild(String message);

    void downloading(int progress);
}
