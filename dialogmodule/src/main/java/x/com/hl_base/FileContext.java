package x.com.hl_base;

import androidx.core.content.FileProvider;

public class FileContext extends FileProvider {
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            AppInit.setContext(getContext());
        }
        return super.onCreate();
    }
}
